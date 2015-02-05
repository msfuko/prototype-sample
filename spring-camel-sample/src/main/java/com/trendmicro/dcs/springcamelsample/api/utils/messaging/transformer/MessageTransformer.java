package com.trendmicro.dcs.springcamelsample.api.utils.messaging.transformer;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.trendmicro.dcs.springcamelsample.api.entity.ContentMessage;
import com.trendmicro.dcs.springcamelsample.api.entity.Message;
@Component
public class MessageTransformer {
	
	public Message transform(String message) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(message, ContentMessage.class);
	}
}
