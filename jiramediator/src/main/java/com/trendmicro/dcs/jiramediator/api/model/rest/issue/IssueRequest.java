package com.trendmicro.dcs.jiramediator.api.model.rest.issue;

import org.springframework.web.util.UriComponentsBuilder;

import com.trendmicro.dcs.jiramediator.api.model.rest.AbstractBaseRequest;

public class IssueRequest extends AbstractBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6410042592008356683L;

	/*
	private String issueKey;
	
	public String getIssueKey() {
		return issueKey;
	}

	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
	}
	 */
	public IssueRequest(String httpHost) {
		super(httpHost);
	}

	@Override
	public void requestUriInit() {
		
		switch (this.requestMethod) {
			case POST:
				requestUri = UriComponentsBuilder.fromUriString(getHttpHost())
					.path("/issue").build().toUri();
				break;
			case GET:
			case PUT:
			case DELETE:
				requestUri = UriComponentsBuilder.fromUriString(getHttpHost())
					.path("/issue/{issueIdOrKey}").build()
					.expand(getKey()).toUri();
				break;
			default:
				throw new UnsupportedOperationException();
		}
	}
	
}
