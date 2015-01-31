package com.trendmicro.dcs.springjmssample.api.utils.messaging;

import java.io.IOException;

import javax.jms.JMSException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.trendmicro.dcs.springjmssample.api.entity.ContentMessage;
import com.trendmicro.dcs.springjmssample.api.entity.SampleMessage;

public interface MessageGateway {
		
		/**
		 * Sending one message
		 * @param message
		 * @throws IOException 
		 * @throws JsonMappingException 
		 * @throws JsonParseException 
		 */
		void send(ContentMessage message) throws JMSException, JsonParseException, JsonMappingException, IOException;
		
		/**
		 * Receive message
		 * @param message
		 * @return message
		 * @throws JMSException 
		 * @throws InterruptedException 
		 * @throws IOException 
		 * @throws JsonMappingException 
		 * @throws JsonParseException 
		 */
		SampleMessage syncReceive() throws JMSException, JsonParseException,
				JsonMappingException, IOException;

		void asyncReceive() throws JMSException, InterruptedException;

}
