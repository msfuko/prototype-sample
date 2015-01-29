package com.trendmicro.dcs.springcamelsample.api.utils.messaging.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.aws.s3.S3Constants;

public class FileMessageProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.getIn().setHeader(S3Constants.KEY, exchange.getIn().getHeader(Exchange.FILE_NAME));
	}
	
}
