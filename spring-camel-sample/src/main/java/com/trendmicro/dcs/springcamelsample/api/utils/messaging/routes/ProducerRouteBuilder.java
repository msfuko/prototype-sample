package com.trendmicro.dcs.springcamelsample.api.utils.messaging.routes;

import javax.annotation.Resource;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trendmicro.dcs.springcamelsample.api.utils.messaging.processors.FileMessageProcessor;
import com.trendmicro.dcs.springcamelsample.api.utils.messaging.processors.FileTransferProcessor;


@Component
public class ProducerRouteBuilder extends RouteBuilder {

	@Resource(name = "producerQueueEndpoint")
	private String queueEndpoint;
	
	@Resource(name = "producerS3Endpoint")
	private String s3Endpoint;
	
	@Resource(name = "fileEndpoint")
	private String fileEndpoint;
	
	@Autowired
	FileTransferProcessor fileTransferProcessor;
	
	/**
	 * Configure producer route - 
	 * 		from seda channel (asynchronous in-memory messaging) to producer endpoint
	 */
	@Override
	public void configure() throws Exception {
		
		from("seda:outboundMessageChannel")
		.errorHandler(deadLetterChannel("log:failedLetter"))
		.choice()
		.when().simple("${body.getClassName} == 'ContentMessage'")
			.routeId("send-sqs")
			.log(LoggingLevel.DEBUG, "[Content] Message starts to send")
			.to(queueEndpoint)
		.otherwise()
			.routeId("s3-folder-route")
			.log(LoggingLevel.DEBUG, "[File] Processing for preparing file")
			.process(fileTransferProcessor);

		from(fileEndpoint)
		.routeId("send-s3")
		.log(LoggingLevel.DEBUG, "[File] Processing for setting header")
		.process(new FileMessageProcessor())
		.log(LoggingLevel.DEBUG, "[File] Message starts to send")
		.errorHandler(deadLetterChannel("log:failedLetter"))
		.to(s3Endpoint);
		
		/*
		from("seda:outboundMessageChannel")
		.routeId("send-sqs")
		.log(LoggingLevel.DEBUG, "[Content] Message starts to send")
		.errorHandler(deadLetterChannel("log:failedLetter"))
		.to(queueEndpoint);
		*/
	}
	
}
