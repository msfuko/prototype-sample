package com.trendmicro.dcs.springjmssample.api.utils.messaging.processor;

import java.io.IOException;

import javax.jms.JMSException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.trendmicro.dcs.springjmssample.api.entity.SampleMessage;
import com.trendmicro.dcs.springjmssample.api.enums.MessageType;

public interface MessageProducerInterface {
	
	void execute(SampleMessage sampleMessage, MessageType messageType) throws JMSException, JsonParseException, JsonMappingException, IOException;
	
}
