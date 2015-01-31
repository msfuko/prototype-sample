package com.trendmicro.dcs.springjmssample.api.utils.messaging.processor;

import java.io.IOException;

import javax.annotation.Resource;
import javax.jms.Connection;
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

@Component("sampleproducer")
public class SampleMessageProducer implements MessageProducerInterface {

	@Resource(name = "connection")
	private Connection connection;
	
	@Resource(name = "queueName")
	private String queueName;
	
	private Log logger = LogFactory.getLog(this.getClass());

	@Override
	public void execute(SampleMessage sampleMessage) throws JMSException,
			JsonParseException, JsonMappingException, IOException {
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(session.createQueue(queueName));
        logger.debug("Send message text - " + sampleMessage.toString());
        Message message = session.createTextMessage(sampleMessage.toString());
		
        producer.send(message);
        logger.debug("Send message " + message.getJMSMessageID());
        session.close();
	}

}
