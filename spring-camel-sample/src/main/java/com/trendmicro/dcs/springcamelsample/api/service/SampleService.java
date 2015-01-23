package com.trendmicro.dcs.springcamelsample.api.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trendmicro.dcs.springcamelsample.api.entity.Message;
import com.trendmicro.dcs.springcamelsample.api.utils.messaging.SampleMessageGateway;

@Service
public class SampleService {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	SampleMessageGateway sampleGateway;
	
	/**
	 * new a sample message 
	 * @return message
	 */
	private Message newSampleMessage() {
		Message message = new Message();
		message.setTicketNumber("CMDEV-256");
		message.setDescription("Please go smoothly");
		Map<String, String> misc = new HashMap<String, String>();
		misc.put("Dept", "DCS");
		misc.put("Component", "CPU");
		message.setMisc(misc);
		return message;
	}

	/**
	 * Example of sending message via message gateway
	 * @return message string
	 */
	public String sendMessage() {
		Message message = this.newSampleMessage();
		sampleGateway.sendMessage(message);
		return message.toString();
	}
}
