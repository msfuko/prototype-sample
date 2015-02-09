package com.trendmicro.dcs.jiramediator.api.dao;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsRequestDAO implements MessageListener, BaseRequestDAO {

	@Override
	public void put() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void get() {
		// TODO Auto-generated method stub
		
	}

	@Override
	@JmsListener(containerFactory = "jmsListenerContainerFactory", destination = "jms_request")
	public void onMessage(Message jmsMessage) {
		String jmsMessageId = null;
				
		try {
			jmsMessageId = jmsMessage.getJMSMessageID();
//				receiveMessage = (Message)((ObjectMessage)jmsMessage).getObject();
			System.out.println("[jmsMessageId] = " + jmsMessageId);
		} catch (JMSException e) {
			System.out.println("can't find message-id and/or correlation-id");
		}
	}

}
