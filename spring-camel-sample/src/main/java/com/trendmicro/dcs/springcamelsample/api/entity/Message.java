package com.trendmicro.dcs.springcamelsample.api.entity;

import java.io.Serializable;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;


public class Message implements Serializable {

	private static final long serialVersionUID = -4747155819588832279L;

	private String ticketNumber;
	
	private String description;

	public String getClassName() {
		return this.getClass().getSimpleName();
	}
	
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
