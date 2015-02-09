package com.trendmicro.dcs.jiramediator.api.model.rest;

public abstract class AbstractBaseRequestBean {
	
	public abstract String getRequestUrl();

	private String requestContent;
	
	public String getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}

}
