package com.trendmicro.dcs.springcamelsample.api.entity;

import java.util.Map;

public class ContentMessage extends Message {
	
	private String content;
	
	private Map<String, String> misc;
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Map<String, String> getMisc() {
		return misc;
	}
	
	public void setMisc(Map<String, String> misc) {
		this.misc = misc;
	}

}
