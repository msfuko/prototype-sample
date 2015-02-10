package com.trendmicro.dcs.jiramediator.api.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class JiraResultBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int responseCode;
	private Object responseContent;
	//private String message;

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
/*
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
*/
	public Object getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(Object responseContent) {
		this.responseContent = responseContent;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
