package com.trendmicro.dcs.jiramediator.api.service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.jms.JMSException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.trendmicro.dcs.jiramediator.api.dao.JmsRequestDAO;
import com.trendmicro.dcs.jiramediator.api.dao.RestRequestDAO;
import com.trendmicro.dcs.jiramediator.api.model.JiraResultBean;
import com.trendmicro.dcs.jiramediator.api.model.rest.function.SearchRequest;
import com.trendmicro.dcs.jiramediator.api.model.rest.issue.IssueRequest;
import com.trendmicro.dcs.jiramediator.api.model.rest.project.ProjectInfoRequest;

@Service
public class JiraIssueService {
	
	@Autowired
	RestRequestDAO restRequestDAO;
	
	@Autowired
	JmsRequestDAO jmsRequestDAO;

	private Log logger = LogFactory.getLog(this.getClass());

	/**
	 * invoke [GET] /jira/rest/api/latest/project
	 * @param request
	 * @return
	 */
	public JiraResultBean getProjectInfo(ProjectInfoRequest request) {
		JiraResultBean result = null;
		try {
			result = restRequestDAO.get(request);
		} catch (Exception ex) {
			throw new RestClientException("Unable to getProjectInfo", ex);
		}
		return result;
	}

	/**
	 * invoke [GET] /jira/rest/api/latest/issue
	 * @param request
	 * @return
	 */
	public JiraResultBean getIssue(IssueRequest request) {
		JiraResultBean result = null;
		try {
			result = restRequestDAO.get(request);
		} catch (Exception ex) {
			throw new RestClientException("Unable to getIssue", ex);
		}
		return result;
	}
	
	/**
	 * insert message to the queue
	 * @param request
	 * @throws JMSException 
	 */
	public void createIssue(IssueRequest request) throws JMSException {
		logger.debug("create async issue");
		try {
			jmsRequestDAO.put(request);
		} catch (Exception ex) {
			throw new JMSException("JMS enqueue error - " + ex.getMessage());
		}
	}

	/**
	 * invoke [POST] /jira/rest/api/latest/issue
	 * @param request
	 */
	public JiraResultBean syncCreateIssue(IssueRequest request) {
		logger.debug("create sync issue");
		JiraResultBean result = null;
		try {
			result = restRequestDAO.post(request);
		} catch (Exception ex) {
			throw new RestClientException("Uable to create issue synchonously", ex);
		}
		return result;
	}
	
	/**
	 * invoke [PUT] /jira/rest/api/latest/issue
	 * @param request
	 * @key should be the same key with this.getIssue()
	 * @return
	 */
	//FIXME
	public JiraResultBean updateIssue(IssueRequest request) {
		JiraResultBean result = null;
		try {
			result = restRequestDAO.put(request);
		} catch (Exception ex) {
			throw new RestClientException("Unable to updateIssue", ex);
		}
		return result;
	}
	
	/**
	 * invoke [POST] /jira/rest/api/latest/search
	 * @param request
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public JiraResultBean searchIssue(SearchRequest request) throws JsonGenerationException, JsonMappingException, IOException {
		//FIXME 
		Map<String, Object> entityMap = new LinkedHashMap<String, Object>();
        
        entityMap.put("jql", request.getJql());
        entityMap.put("startAt", request.getStartAt());
        entityMap.put("maxResults", request.getMaxResults());
        entityMap.put("fields", request.getFields());
        
        request.setPayload(new ObjectMapper().writeValueAsString(entityMap));
        JiraResultBean result = null;
        try {
        	result = restRequestDAO.post(request);
        } catch (Exception ex) {
        	throw new RestClientException("Unable to searchIssue", ex);
        }
        return result;
	}
	
	
	//TODO: delete
	//@CacheEvict
}
