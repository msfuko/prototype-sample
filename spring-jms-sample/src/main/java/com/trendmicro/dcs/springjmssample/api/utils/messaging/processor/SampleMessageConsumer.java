package com.trendmicro.dcs.springjmssample.api.utils.messaging.processor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

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

	public SampleMessage execute(Message message) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ContentMessage contentMessage = null;
	    try {
	    	if (message != null) {
	    		TextMessage txtMessage = (TextMessage) message;
	    		System.err.println(txtMessage.getText());
	    		contentMessage = mapper.readValue(txtMessage.getText(), ContentMessage.class);
	    		message.acknowledge();
	    		System.out.println("Acknowledged: " + message.getJMSMessageID());  //do something
	    		System.out.println("Acknowledged: " + contentMessage.getDescription());  //do something
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
