package com.trendmicro.dcs.springcamelsample.api.dao;

import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.trendmicro.dcs.springcamelsample.api.entity.ContentMessage;
import com.trendmicro.dcs.springcamelsample.api.entity.Message;

//use explicity declare at spring-jms-config.xml due to register to MessageListenerContainer
@Component
public class JmsRequestDAO implements MessageListener{
	
	@Autowired
	private ConnectionFactory connectionFactory;
	
	@Autowired
	private String jmsRequestQueueName;
	
	@Autowired
	private JmsResponseDAO jmsResponseDAO;

	/*
	 * enqueue operation
	 * 
	 * call this method to send a message to queue
	 */
	public void enqueue(final Message message, final String messageId, final String correlationId){
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		
		jmsTemplate.send(jmsRequestQueueName, new MessageCreator() {
	            public javax.jms.Message createMessage(Session session) throws JMSException {
	            	javax.jms.Message jmsMessage;
	            	
	            	//jmsMessage = session.createTextMessage(message.toString());
	            	jmsMessage = session.createObjectMessage(message); //send message as Serialized Object
	            	jmsMessage.setJMSMessageID(messageId);
	            	jmsMessage.setJMSCorrelationID(correlationId);
	            	
	            	return jmsMessage;
	            }
	    });
	 }
	
	/*
	 * asynchronous reception
	 * 
	 * it is handled by MessageListenerContainer (by spring)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@JmsListener(containerFactory = "jmsListenerContainerFactory", destination = "jms_request")
	public void onMessage(javax.jms.Message jmsMessage) {
		Message receiveMessage, replyMessage; 
		String jmsMessageId = null;
		String jmsCorrelationId = null;
				
		try {
			jmsMessageId = jmsMessage.getJMSMessageID();
			jmsCorrelationId = jmsMessage.getJMSCorrelationID();
			System.out.println("[jmsMessageId] = " + jmsMessageId);
			System.out.println("[jmsCorrelationId] = " + jmsCorrelationId);
		} catch (JMSException e) {
			System.out.println("can't find message-id and/or correlation-id");
		}

		/*
		 * message should be Serialized object
		 */
		if ( jmsMessage instanceof ObjectMessage){
			try {
				receiveMessage = (Message)((ObjectMessage)jmsMessage).getObject();
				System.out.println("*** received message --> " + receiveMessage.toString() + " ***");

				replyMessage = this.dummyReplyMessage();
				jmsResponseDAO.enqueue(replyMessage, jmsMessageId, jmsCorrelationId);
				
			} catch (JMSException e) {
				System.out.println("receive a wrong object");
			}
		}
		else{
			System.out.println("receive a wrong message");
		}
	}
	
	private Message dummyReplyMessage(){
		ContentMessage message = new ContentMessage();
		message.setTicketNumber("CMDEV-512");
		message.setDescription("ok");
		Map<String, String> misc = new HashMap<String, String>();
		misc.put("Dept", "DCS-RD");
		misc.put("Component", "Mouse");
		message.setMisc(misc);
		
		return message;
	}
}
