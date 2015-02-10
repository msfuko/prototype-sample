package com.trendmicro.dcs.jiramediator.api.model.rest;

import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.springframework.web.util.UriComponentsBuilder;

public class IssueRequest extends AbstractBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6410042592008356683L;

	private String issueKey;
	
	public String getIssueKey() {
		return issueKey;
	}

	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
		this.requestUriInit();
	}

	public IssueRequest(String httpHost, String payload) {
		super(httpHost);
		this.setPayload(payload);
		this.requestUriInit();
	}

	@Override
	public void requestUriInit() {
		if (StringUtils.isBlank(getIssueKey())) {
			requestUri = UriComponentsBuilder.fromUriString(getHttpHost())
					.path("/issue/{issueIdOrKey}/editmeta").build()
					.expand(getIssueKey()).toUri();
		} else {
			requestUri = UriComponentsBuilder.fromUriString(getHttpHost())
				.path("/issue").build().toUri();
		}
	}
	
}
