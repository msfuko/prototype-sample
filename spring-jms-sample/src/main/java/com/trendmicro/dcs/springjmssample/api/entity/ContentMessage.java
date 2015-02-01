package com.trendmicro.dcs.springjmssample.api.entity;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.codehaus.jackson.map.ObjectMapper;

public class ContentMessage extends SampleMessage {
	
	private Map<String, String> misc;
	
	public Map<String, String> getMisc() {
		return misc;
	}
	
	public void setMisc(Map<String, String> misc) {
		this.misc = misc;
	}
	
	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (Exception ex) {
			return "";
		}
	}

}
