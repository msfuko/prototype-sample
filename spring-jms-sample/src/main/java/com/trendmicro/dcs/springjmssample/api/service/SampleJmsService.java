package com.trendmicro.dcs.springjmssample.api.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.trendmicro.dcs.springjmssample.api.entity.ContentMessage;
import com.trendmicro.dcs.springjmssample.api.utils.messaging.SampleMessageGateway;

@Service
public class SampleJmsService {
	
	@Autowired
	SampleMessageGateway messageGateway;
	
	/**
	 * new a sample message 
	 * @return message
	 */
	private ContentMessage newSampleMessage() {
		ContentMessage message = new ContentMessage();
		message.setTicketNumber("CMDEV-256");
		message.setDescription("Please go smoothly");
		
		// set current timestamp
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Map<String, String> misc = new HashMap<String, String>();
		misc.put("Dept", "DCS");
		misc.put("Component", "CPU");
		misc.put("TimeStamp", format.format(now));
		message.setMisc(misc);
		
		return message;
	}
	
	public String send() throws JMSException, JsonParseException, JsonMappingException, IOException {
		ContentMessage message = this.newSampleMessage();
		messageGateway.send(message);
		return message.toString();
	}

	public String syncReceive() throws JMSException, JsonParseException, JsonMappingException, IOException {
		ContentMessage message = (ContentMessage) messageGateway.syncReceive();
		if (message != null) 
			return message.toString();
		else
			return "Nothing in Queue!";
	}
	
	public void asyncReceive() throws JMSException, InterruptedException {
		messageGateway.asyncReceive();
	}
}
