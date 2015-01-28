package com.trendmicro.dcs.springcamelsample.api.utils.messaging.routes;

import javax.annotation.Resource;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class ProducerRouteBuilder extends RouteBuilder {

	@Resource(name = "producerQueueEndpoint")
	private String queueEndpoint;
	
	/**
	 * Configure producer route - 
	 * 		from seda channel (asynchronous in-memory messaging) to producer endpoint
	 */
	@Override
	public void configure() throws Exception {
		from("seda:outboundMessageChannel")
		.routeId("send-sqs")
		.log(LoggingLevel.INFO, "Message starts to send")
		.errorHandler(deadLetterChannel("log:failedLetter"))
		.to(queueEndpoint);
	}
	
}
