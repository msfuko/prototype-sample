package com.trendmicro.dcs.jiramediator.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.trendmicro.dcs.jiramediator.api.dao.JmsRequestDAO;
import com.trendmicro.dcs.jiramediator.api.dao.RestRequestDAO;
import com.trendmicro.dcs.jiramediator.api.model.JiraResultBean;
import com.trendmicro.dcs.jiramediator.api.model.rest.IssueRequest;
import com.trendmicro.dcs.jiramediator.api.model.rest.ProjectInfoRequest;

@Service
public class JiraIssueService {
	
	@Autowired
	RestRequestDAO restRequestDAO;
	
	@Autowired
	JmsRequestDAO jmsRequestDAO;
	
	@Cacheable(value = "default", key = "#request.projectKey")
	public JiraResultBean getProjectInfo(ProjectInfoRequest request) {
		System.out.println("REQUEST in " + request.getRequestUri());
		return restRequestDAO.get(request);
	}
	
	//Does creation needs to cache?
	public void createIssue(IssueRequest request) {
		request.setRequestMethod(HttpMethod.POST);
		jmsRequestDAO.put(request);
		//return restRequestDAO.post(request);
	}
	
	//Does creation needs to cache?
	public void updateIssue(IssueRequest request) {
		request.setRequestMethod(HttpMethod.PUT);
		jmsRequestDAO.put(request);
	}

	//TODO: delete
}
