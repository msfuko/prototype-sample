package com.trendmicro.dcs.jiramediator.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trendmicro.dcs.jiramediator.api.dao.RestRequestDAO;
import com.trendmicro.dcs.jiramediator.api.model.JiraResultBean;
import com.trendmicro.dcs.jiramediator.api.model.rest.ProjectInfoRequestBean;

@Service
public class JiraIssueService {
	
	@Autowired
	RestRequestDAO restRequestDAO;
	
	
	public JiraResultBean getProjectInfo(ProjectInfoRequestBean request) {
		//return restRequestDAO.get(request, JiraResultBean.class);
		return null;
	}
}
