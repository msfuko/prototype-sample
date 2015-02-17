package com.trendmicro.dcs.jiramediator.api.model.rest.project;


import org.springframework.web.util.UriComponentsBuilder;

import com.trendmicro.dcs.jiramediator.api.model.rest.AbstractBaseRequest;

public class ProjectInfoRequest extends AbstractBaseRequest {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2750385092436146327L;

	public ProjectInfoRequest(String httpHost) {
		super(httpHost);
	}

	@Override
	public void requestUriInit() {
		this.requestUri = UriComponentsBuilder.fromUriString(getHttpHost())
				.path("/project/{projectIdOrKey}").build()
				.expand(getKey()).toUri();
	}

}
