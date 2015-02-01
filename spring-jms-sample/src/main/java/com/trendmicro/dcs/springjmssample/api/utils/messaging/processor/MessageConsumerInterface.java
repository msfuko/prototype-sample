package com.trendmicro.dcs.springjmssample.api.utils.messaging.processor;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.trendmicro.dcs.springjmssample.api.entity.SampleMessage;

public interface MessageConsumerInterface {
	
	SampleMessage execute() throws JMSException, JsonParseException, JsonMappingException, IOException;
	
	SampleMessage execute(Message message) throws JsonParseException, JsonMappingException, IOException;
}
