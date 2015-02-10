package com.trendmicro.dcs.jiramediator.api.model.rest;


import org.springframework.web.util.UriComponentsBuilder;

public class ProjectInfoRequest extends AbstractBaseRequest {
	
	private String projectKey;

	public ProjectInfoRequest(String httpHost) {
		super(httpHost);
	}

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
		this.requestUriInit();
	}

	@Override
	public void requestUriInit() {
		this.requestUri = UriComponentsBuilder.fromUriString(getHttpHost())
				.path("/project/{projectIdOrKey}").build()
				.expand(getProjectKey()).toUri();
	}

}
