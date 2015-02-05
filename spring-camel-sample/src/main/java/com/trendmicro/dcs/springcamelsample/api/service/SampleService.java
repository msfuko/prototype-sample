package com.trendmicro.dcs.springcamelsample.api.service;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trendmicro.dcs.springcamelsample.api.entity.ContentMessage;
import com.trendmicro.dcs.springcamelsample.api.entity.FileMessage;
import com.trendmicro.dcs.springcamelsample.api.entity.Message;
import com.trendmicro.dcs.springcamelsample.api.utils.messaging.MessageGatewayImpl;

@Service
public class SampleService {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	MessageGatewayImpl messageGateway;
	
	/**
	 * new a sample message 
	 * @return message
	 */
	private Message newSampleMessage() {
		ContentMessage message = new ContentMessage();
		message.setTicketNumber("CMDEV-256");
		message.setDescription("Please go smoothly");
		Map<String, String> misc = new HashMap<String, String>();
		misc.put("Dept", "DCS");
		misc.put("Component", "CPU");
		message.setMisc(misc);
		return message;
	}
	
	private Message newSampleFileMessage() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("/tmp/happy-file-test.txt", "UTF-8");
		writer.println("The first line");
		writer.println("The second line");
		writer.close();
		FileMessage message = new FileMessage();
		message.setTicketNumber("CMDEV-256");
		message.setDescription("It's a sunshine day");
		message.setFilePath("/tmp/happy-file-test.txt");
		return message;
	}
	
	
	/**
	 * Example of sending message via message gateway
	 * @return message string if success, null if exception happens
	 */
	public Message sendMessage() {
		Message message = this.newSampleMessage();
		try {
			messageGateway.send(message);
			return message;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Example of sending file message via message gateway
	 * @return message string if success, null if exception happens
	 */
	public String sendFile() throws FileNotFoundException, UnsupportedEncodingException {
		Message message = this.newSampleFileMessage();
		try {
			messageGateway.send(message);
			return message.toString();
		} catch (Exception ex) {
			return null;
		}
	}

	@Handler
	public void receiveMessage(Message message) {
		//do something
		System.out.println("Show to UI - ticket number " + message.getTicketNumber());
		System.out.println("Show to UI - ticket description" + message.getDescription());
	}
	
}
