package com.trendmicro.dcs.springcamelsample.api.dao;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.trendmicro.dcs.springcamelsample.api.entity.Message;

@Component
public class JmsResponseDAO {

	@Autowired
	private ConnectionFactory connectionFactory;
	
	@Autowired
	private String jmsResponseQueueName;

	@Autowired
	private Integer jmsResponseReceiveTimeout;
	
	/*
	 * enqueue operation
	 * 
	 * call this method directly to send a message to queue
	 */
	public void enqueue(final Message message, final String messageId, final String correlationId){
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		
		jmsTemplate.send(jmsResponseQueueName, new MessageCreator() {
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
	 * synchronous reception
	 */
	public Message dequeue(){
		JmsTemplate jmsTemplate;
		javax.jms.Message jmsMessage=null;
		Message receiveMessage = null;

		//init JmsTemplate
		jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setReceiveTimeout(jmsResponseReceiveTimeout);
		
		//3 times retry
		for(int i=0; i<3; i++){
			jmsMessage = jmsTemplate.receive(jmsResponseQueueName);
			if (jmsMessage != null){
				break;
			}
			try {
				System.out.println("--- no reply message yet! ---");
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		
		/*
		 * message should be Serialized Object
		 */
		if ( (jmsMessage != null) && (jmsMessage instanceof ObjectMessage)){
			try {
				receiveMessage = (Message)((ObjectMessage)jmsMessage).getObject();
				System.out.println("*** received message *** : " + receiveMessage);
				
				return receiveMessage;
				
			} catch (JMSException e) {
				System.out.println("+++ received wrong reply, give up +++");
				return null;
			}
		}
		//no message 
		else{
			System.out.println("+++ give up +++");
			
			return null;
		}

	}

}
