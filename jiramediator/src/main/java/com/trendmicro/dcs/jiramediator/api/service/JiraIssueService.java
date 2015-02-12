package com.trendmicro.dcs.jiramediator.api.service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

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
	
	@Cacheable(value = "default", key = "{#root.methodName, #request.projectKey}")
	public JiraResultBean getProjectInfo(ProjectInfoRequest request) {
		System.out.println("REQUEST get project");
		request.setRequestMethod(HttpMethod.GET);
		return restRequestDAO.get(request);
	}
	
	@Cacheable(value = "default", key = "{#root.methodName, #request.issueKey}")
	public JiraResultBean getIssue(IssueRequest request) {
		System.out.println("REQUEST get issue");
		request.setRequestMethod(HttpMethod.GET);
		return restRequestDAO.get(request);
	}
	
	public void createIssue(IssueRequest request) {
		request.setRequestMethod(HttpMethod.POST);
		jmsRequestDAO.put(request);
	}

	public JiraResultBean syncCreateIssue(IssueRequest request) {
		request.setRequestMethod(HttpMethod.POST);
		return restRequestDAO.post(request);
	}
	
	@CachePut(value = "default", key = "new String({#root.methodName, #request.issueKey})")
	public JiraResultBean updateIssue(IssueRequest request) {
		request.setRequestMethod(HttpMethod.PUT);
		//jmsRequestDAO.put(request);
		return restRequestDAO.put(request);
	}
	
	@Cacheable(value = "default", key = "{#root.methodName, #request.jql, #request.startAt, #request.maxResults, #request.fields}")
	public JiraResultBean searchIssue(SearchRequest request) throws JsonGenerationException, JsonMappingException, IOException {
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
