package com.trendmicro.dcs.jiramediator.api.model.rest;


import java.io.Serializable;
import java.net.URI;

import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public abstract class AbstractBaseRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1542558702414558768L;

	public AbstractBaseRequest(String httpHost) {
		this.httpHost = httpHost;
	}
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	protected String httpHost;
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	protected URI requestUri;
	
	protected HttpMethod requestMethod;

	protected String payload;
	
	public abstract void requestUriInit();
	
	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public HttpMethod getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(HttpMethod requestMethod) {
		this.requestMethod = requestMethod;
	}

	public URI getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(URI requestUri) {
		this.requestUri = requestUri;
	}

	public String getHttpHost() {
		return this.httpHost;
	}
	
}
