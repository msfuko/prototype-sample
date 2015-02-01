package com.trendmicro.dcs.springjmssample.api.utils.messaging.processor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.trendmicro.dcs.springjmssample.api.entity.ContentMessage;
import com.trendmicro.dcs.springjmssample.api.entity.SampleMessage;

@Component("sampleconsumer")
public class SampleMessageConsumer implements MessageConsumerInterface {

	@Resource(name = "connection")
	private Connection connection;
	
	@Resource(name = "timeout")
	private Integer timeout;

	@Resource(name = "queueName")
	private String queueName;

	@Resource(name = "invalidQueueName")
	private String invalidQueueName;

	@Resource(name = "replyQueueName")
	private String replyQueueName;
	
	private Log logger = LogFactory.getLog(this.getClass());

	public void checkReplyMessage(Message message) throws JMSException {
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		/*
		 * FIXME: I could not make it work by getting JMSReplyTo header from message.
		 *        It is always null although I've set it in MessageProducer 
		 *        Not sure it's supported or not :-(
		 *        Ref:
		 *        http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/jmsclient.html#jmsclient-ref
		 *        https://github.com/awslabs/amazon-sqs-java-messaging-lib/blob/master/src/main/java/com/amazon/sqs/javamessaging/message/SQSMessage.java
		 */
		if (true) {
		//if (message.getJMSReplyTo() != null) {
			//Destination replyDestination = message.getJMSReplyTo();
			//MessageProducer replyProducer = session.createProducer(replyDestination);
			
			// Using below way as workaround
			MessageProducer replyProducer = session.createProducer(session.createQueue(replyQueueName));
			
			
			// Set the content of reply message
			TextMessage replyMessage = session.createTextMessage();
			replyMessage.setText("test response of " + message.getJMSMessageID());
			
			// Set the correlation ID from the received message to be the correlation id of the response message
            // this lets the client identify which message this is a response to if it has more than
            // one outstanding message to the server
			replyMessage.setJMSCorrelationID(message.getJMSMessageID());
			
			// Send the response to the Destination specified by the JMSReplyTo field of the received message,
            // this is presumably a temporary queue created by the client
			replyProducer.send(replyMessage);
		} 
		/*
		else {
			// Send message to invalid queue
			MessageProducer invalidProducer = session.createProducer(session.createQueue(invalidQueueName));
			message.setJMSCorrelationID(message.getJMSMessageID());
			invalidProducer.send(message);
		}
		*/
		session.close();
	}
	
	
	public SampleMessage execute(Message message) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ContentMessage contentMessage = null;
	    try {
	    	if (message != null) {
	    		// FIXME assume it' text message for now
	    		contentMessage = mapper.readValue(((TextMessage)message).getText(), ContentMessage.class);
	    		
	    		// Check to send reply message
	    		this.checkReplyMessage(message);
	    		
	    		// Do something
	    		logger.info("Acknowledged: " + message.getJMSMessageID());

	    		// Acknowledge
	    		message.acknowledge();
	    	}
	     } catch (JMSException e) {
            System.err.println("Error receiving from SQS: " + e.getMessage());
            e.printStackTrace();
	     }
	    return contentMessage;
	}

	@Override
	public SampleMessage execute() throws JMSException, JsonParseException, JsonMappingException, IOException {
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer consumer = session.createConsumer(session.createQueue(queueName));
		SampleMessage message = this.execute(consumer.receive(TimeUnit.MINUTES.toMillis(this.timeout)));
		session.close();
		return message;
	}

}
