package com.trendmicro.dcs.springjmssample.api.utils.messaging;

import java.io.IOException;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.trendmicro.dcs.springjmssample.api.entity.ContentMessage;
import com.trendmicro.dcs.springjmssample.api.entity.SampleMessage;
import com.trendmicro.dcs.springjmssample.api.utils.messaging.processor.SampleMessageConsumer;
import com.trendmicro.dcs.springjmssample.api.utils.messaging.processor.SampleMessageProducer;

@Component
public class SampleMessageGateway implements MessageGateway {
	
	@Resource(name = "connection")
	private Connection connection;
	
	@Resource(name = "queueName")
	private String queueName;
	
	@Resource(name = "timeout")
	private Integer timeout;

	@Autowired
	SampleMessageProducer producer;
	
	@Autowired
	SampleMessageConsumer consumer;

	@Autowired
	SampleMessageConsumerListener consumerListener;
	
	private void ensureQueueExists(SQSConnection connection, String queueName) throws JMSException {
		if(connection instanceof SQSConnection) {
			AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();
			try {
				if(!client.queueExists(queueName)) {
					client.createQueue(queueName);
				} 
			} catch (Exception ex) {
				//pass
			}
		}
	}
	
	@Override
	public void send(ContentMessage contentMessage) throws JMSException, JsonParseException, JsonMappingException, IOException {
		// Make sure queue exists
		if(connection instanceof SQSConnection) {
            ensureQueueExists((SQSConnection)connection, queueName);
        }
		producer.execute(contentMessage);
	}

	@Override
	public SampleMessage syncReceive() throws JMSException, JsonParseException, JsonMappingException, IOException {
		return this.consumer.execute();
	}

	@Override
	public void asyncReceive() throws JMSException, InterruptedException {
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer consumer = session.createConsumer(session.createQueue(queueName));
		consumer.setMessageListener(consumerListener);
		connection.start();
		Thread.sleep(timeout * 1000);
	}
	

}
