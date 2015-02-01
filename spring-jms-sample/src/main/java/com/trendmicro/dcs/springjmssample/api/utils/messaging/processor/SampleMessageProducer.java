package com.trendmicro.dcs.springjmssample.api.utils.messaging.processor;

import java.io.IOException;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.stereotype.Component;

import com.trendmicro.dcs.springjmssample.api.entity.SampleMessage;
import com.trendmicro.dcs.springjmssample.api.enums.MessageType;

@Component("sampleproducer")
public class SampleMessageProducer implements MessageProducerInterface {

	@Resource(name = "connection")
	private Connection connection;
	
	@Resource(name = "queueName")
	private String queueName;
	
	@Resource(name = "replyQueueName")
	private String replyQueueName;
	
	private Log logger = LogFactory.getLog(this.getClass());

	@Override
	public void execute(SampleMessage sampleMessage, MessageType messageType) throws JMSException,
			JsonParseException, JsonMappingException, IOException {
		
		// Setup a message producer to send message to the queue 
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(session.createQueue(queueName));
        logger.debug("Send message text - " + sampleMessage.toString());
        
        // Create a message that we want to send
        Message message = session.createTextMessage(sampleMessage.toString());
        
        // Set reply destination
        if (messageType.equals(MessageType.REQUEST_REPLY)) {
        	message.setJMSReplyTo(session.createQueue(replyQueueName));
        }
        producer.send(message);
        
        logger.info("Send message " + message.getJMSMessageID() + " with reply queue " + message.getJMSReplyTo().toString());
        session.close();
	}

}
