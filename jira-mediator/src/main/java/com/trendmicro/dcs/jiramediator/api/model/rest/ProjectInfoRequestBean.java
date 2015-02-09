package com.trendmicro.dcs.jiramediator.api.model.rest;

public class ProjectInfoRequestBean extends AbstractBaseRequestBean {
	
	@Override
	public String getRequestUrl() {
		return "/project/";
	}
	
	private String projectKey;

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}
	
}
