package com.trendmicro.dcs.springjmssample.api.entity;

import java.io.Serializable;
import java.util.Map;

public class SampleMessage implements Serializable {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	private String ticketNumber;
	
	private String description;

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
}
