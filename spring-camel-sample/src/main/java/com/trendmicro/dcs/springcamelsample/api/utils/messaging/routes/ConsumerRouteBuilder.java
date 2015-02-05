package com.trendmicro.dcs.springcamelsample.api.utils.messaging.routes;

import javax.annotation.Resource;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.trendmicro.dcs.springcamelsample.api.service.SampleService;

@Component
public class ConsumerRouteBuilder extends RouteBuilder {
	
	@Resource(name = "producerQueueEndpoint")
	private String requestQueueEndpoint;
	
	@Resource(name = "consumerQueueEndpoint")
	private String replyQueueEndpoint;
	
	/**
	 * Configure consumer route - 
	 * 		from Consumer endpoint to message proxy
	 */	
	@Override
	public void configure() throws Exception {
		from(requestQueueEndpoint)
		.routeId("recv-request-send-response-sqs")
		.log(LoggingLevel.INFO, "Start to receive message")
		.errorHandler(deadLetterChannel("log:ConsumerfailedLetter"))
		//.bean(SampleService.class, "receiveMessage")
		.to(replyQueueEndpoint);
	}
	
}
