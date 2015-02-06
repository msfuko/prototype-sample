package com.trendmicro.dcs.springcamelsample.api.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trendmicro.dcs.springcamelsample.api.dao.JmsResponseDAO;
import com.trendmicro.dcs.springcamelsample.api.dao.JmsRequestDAO;
import com.trendmicro.dcs.springcamelsample.api.entity.Message;

@Service
public class JmsDemoService {
	
	@Autowired
	private JmsRequestDAO producer;
	
	@Autowired
	private JmsResponseDAO consumer;
	
	public Message getJIRATickets(Message message){
		String correlationId;
		
		//prepare correlationId
		correlationId = UUID.randomUUID().toString();
		
		//send
		producer.enqueue(message, "JmsDemoService", correlationId);
		
		//polling return queue
		return consumer.dequeue(correlationId);
	}
}
