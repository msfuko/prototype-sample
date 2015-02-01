package com.trendmicro.dcs.springjmssample.web.controller;

import java.io.IOException;

import javax.jms.JMSException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trendmicro.dcs.springjmssample.api.service.SampleJmsService;

@Controller
@RequestMapping(value = "/rest")
public class SampleController {
	
	@Autowired
	SampleJmsService sampleService;
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/message/jms-sqs/test/send")
	public String testMessageSQSSender() throws JMSException {
		logger.debug("test message sqs is called");
		try {
			return sampleService.send();
		} catch (Exception e) {
		}
		return "Sorry, something went wrong Q__Q ";
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/message/jms-sqs/test/sync-receive")
	public String testMessageSQSSyncReceiver() throws JMSException, JsonParseException, JsonMappingException, IOException {
		logger.debug("test message sqs is called for receiving");
		try {
			return sampleService.syncReceive();
		} catch (Exception e) {
			//pass
		}
		return "Sorry, something went wrong Q__Q ";
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/message/jms-sqs/test/async-receive")
	public String testMessageSQSAsyncReceiver() throws JMSException, JsonParseException, JsonMappingException, IOException {
		logger.debug("test message sqs is called for receiving");
		try {
			sampleService.asyncReceive();
		} catch (Exception e) {
			//pass
		}
		return "It's an async test!";
	}
}
