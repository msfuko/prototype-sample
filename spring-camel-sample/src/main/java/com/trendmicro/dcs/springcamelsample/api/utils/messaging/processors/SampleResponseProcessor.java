package com.trendmicro.dcs.springcamelsample.api.utils.messaging.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.trendmicro.dcs.springcamelsample.api.entity.ContentMessage;
@Component
public class SampleResponseProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		//TODO only test on CententMessage for now
		ContentMessage message = (ContentMessage) exchange.getIn().getBody();
		message.setCorrelationId(exchange.getExchangeId());
	}

}
