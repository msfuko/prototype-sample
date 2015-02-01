package com.trendmicro.dcs.springjmssample.api.enums;

public enum MessageType {
		
	NORMAL("send-and-forget"),
	REQUEST_REPLY("request-and-reply");
	
	private String description;
	
	private MessageType(String description) {
		this.description = description;
	}
}
