package com.trendmicro.dcs.jiramediator.api.service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

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
	@Cacheable(value = "default", key = "{#root.methodName, #request.projectKey}")
	public JiraResultBean getProjectInfo(ProjectInfoRequest request) {
		logger.debug("cache miss - " + request.toString());
		request.setRequestMethod(HttpMethod.GET);
		return restRequestDAO.get(request);
	}

	/**
	 * invoke [GET] /jira/rest/api/latest/issue
	 * @param request
	 * @return
	 */
	@Cacheable(value = "default", key = "{#root.methodName, #request.issueKey}")
	public JiraResultBean getIssue(IssueRequest request) {
		logger.debug("cache miss - " + request.toString());
		request.setRequestMethod(HttpMethod.GET);
		return restRequestDAO.get(request);
	}
	
	/**
	 * insert message to the queue
	 * @param request
	 */
	public void createIssue(IssueRequest request) {
		logger.debug("create async issue");
		request.setRequestMethod(HttpMethod.POST);
		jmsRequestDAO.put(request);
	}

	/**
	 * invoke [POST] /jira/rest/api/latest/issue
	 * @param request
	 */
	public JiraResultBean syncCreateIssue(IssueRequest request) {
		logger.debug("create sync issue");
		request.setRequestMethod(HttpMethod.POST);
		return restRequestDAO.post(request);
	}
	
	/**
	 * invoke [PUT] /jira/rest/api/latest/issue
	 * @param request
	 * @return
	 */
	@CachePut(value = "default", key = "new String({#root.methodName, #request.issueKey})")
	public JiraResultBean updateIssue(IssueRequest request) {
		logger.debug("update cache - " + request.toString());
		request.setRequestMethod(HttpMethod.PUT);
		return restRequestDAO.put(request);
	}
	
	/**
	 * invoke [POST] /jira/rest/api/latest/search
	 * @param request
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Cacheable(value = "default", key = "{#root.methodName, #request.jql, #request.startAt, #request.maxResults, #request.fields}")
	public JiraResultBean searchIssue(SearchRequest request) throws JsonGenerationException, JsonMappingException, IOException {
		logger.debug("cache miss - " + request.toString());
		request.setRequestMethod(HttpMethod.POST);
		Map<String, Object> entityMap = new LinkedHashMap<String, Object>();
        
        entityMap.put("jql", request.getJql());
        entityMap.put("startAt", request.getStartAt());
        entityMap.put("maxResults", request.getMaxResults());
        entityMap.put("fields", request.getFields());
        
        request.setPayload(new ObjectMapper().writeValueAsString(entityMap));
        return restRequestDAO.post(request);
	}
	
	
	//TODO: delete
	//@CacheEvict
}
