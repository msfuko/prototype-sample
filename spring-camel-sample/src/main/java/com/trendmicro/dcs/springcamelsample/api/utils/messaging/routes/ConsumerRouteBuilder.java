package com.trendmicro.dcs.springcamelsample.api.utils.messaging.routes;

import javax.annotation.Resource;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.trendmicro.dcs.springcamelsample.api.utils.messaging.MessageGatewayImpl;

@Component
public class ConsumerRouteBuilder extends RouteBuilder {

	@Resource(name = "consumerQueueEndpoint")
	private String queueEndpoint;
	
	/**
	 * Configure consumer route - 
	 * 		from Consumer endpoint to message proxy
	 */	
	@Override
	public void configure() throws Exception {
		from(queueEndpoint)
		.routeId("recv-sqs")
		.log(LoggingLevel.INFO, "Start to receive message")
		.errorHandler(deadLetterChannel("log:failedLetter"))
		.bean(MessageGatewayImpl.class, "receiveMessage");
	}
	
}
