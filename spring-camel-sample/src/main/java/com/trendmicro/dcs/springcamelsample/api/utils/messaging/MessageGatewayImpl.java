package com.trendmicro.dcs.springcamelsample.api.utils.messaging;

import java.util.Collection;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

import com.trendmicro.dcs.springcamelsample.api.entity.Message;

@Component
public class MessageGatewayImpl implements MessageGateway {
	
	/**
	 * Declare a producer endpoint
	 */
	@Produce(uri = "direct:outboundMessageChannel")
	ProducerTemplate producerTemplate;
	
	@Override
	public void send(Message message) {
		producerTemplate.sendBody(message);
	}

	@Override
	public void send(Collection<Message> messages) {
		producerTemplate.sendBody(messages);
	}

}
