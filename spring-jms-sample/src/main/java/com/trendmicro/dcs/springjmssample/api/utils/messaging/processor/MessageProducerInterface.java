package com.trendmicro.dcs.springjmssample.api.utils.messaging.processor;

import java.io.IOException;

import javax.jms.JMSException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.trendmicro.dcs.springjmssample.api.entity.SampleMessage;

public interface MessageProducerInterface {
	
	void execute(SampleMessage sampleMessage) throws JMSException, JsonParseException, JsonMappingException, IOException;
	
}
