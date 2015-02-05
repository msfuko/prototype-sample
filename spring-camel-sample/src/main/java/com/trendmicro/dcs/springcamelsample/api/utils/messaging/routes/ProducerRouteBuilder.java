package com.trendmicro.dcs.springcamelsample.api.utils.messaging.routes;

import javax.annotation.Resource;

import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trendmicro.dcs.springcamelsample.api.service.SampleService;
import com.trendmicro.dcs.springcamelsample.api.utils.messaging.processors.FileMessageProcessor;
import com.trendmicro.dcs.springcamelsample.api.utils.messaging.processors.FileTransferProcessor;
import com.trendmicro.dcs.springcamelsample.api.utils.messaging.processors.SampleResponseProcessor;
import com.trendmicro.dcs.springcamelsample.api.utils.messaging.transformer.MessageTransformer;


@Component
public class ProducerRouteBuilder extends RouteBuilder {

	@Resource(name = "producerQueueEndpoint")
	private String requestQueueEndpoint;
	
	@Resource(name = "producerS3Endpoint")
	private String s3Endpoint;
	
	@Resource(name = "fileEndpoint")
	private String fileEndpoint;
	
	@Resource(name = "consumerQueueEndpoint")
	private String replyQueueEndpoint;
	
	@Autowired
	SampleResponseProcessor sampleResponseProcessor;
	
	@Autowired
	FileTransferProcessor fileTransferProcessor;
	
	@Autowired
	MessageTransformer messageTransformer;
	
	/**
	 * Configure producer route - 
	 * 		from seda channel (asynchronous in-memory messaging) to producer endpoint
	 */
	@Override
	public void configure() throws Exception {
		
		from("direct:outboundMessageChannel")
		.errorHandler(deadLetterChannel("log:failedLetter"))
		.choice()
		.when().simple("${body.getClassName} == 'ContentMessage'")
			.routeId("send-request-sqs")
			.setExchangePattern(ExchangePattern.InOut)	//FIXME - seems like no use lol
			.log(LoggingLevel.INFO, "[Content] Message starts to send")
			.to(requestQueueEndpoint)
		.otherwise()
			.routeId("s3-folder-route")
			.log(LoggingLevel.INFO, "[File] Processing for preparing file")
			.process(fileTransferProcessor);

		from(fileEndpoint)
		.routeId("send-request-s3")
		.log(LoggingLevel.INFO, "[File] Processing for setting header")
		.process(new FileMessageProcessor())
		.log(LoggingLevel.INFO, "[File] Message starts to send")
		.errorHandler(deadLetterChannel("log:failedLetter"))
		.to(s3Endpoint);
		
		from(replyQueueEndpoint)
		.routeId("recv-response-sqs")
		.log(LoggingLevel.INFO, "[Content] receive responce")
		.errorHandler(deadLetterChannel("log:ProducerfailedLetter"))
		//.process(new SampleResponseProcess())
		//.filter("${body.getCorrelationId} == true")
		.transform().method("messageTransformer", "transform")
		.log(LoggingLevel.INFO, "[Content] going to processing received message")
		.bean(SampleService.class, "receiveMessage");
	}
	
}
