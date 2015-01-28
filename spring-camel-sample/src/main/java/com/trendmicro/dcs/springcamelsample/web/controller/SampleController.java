package com.trendmicro.dcs.springcamelsample.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trendmicro.dcs.springcamelsample.api.service.SampleService;

@Controller
@RequestMapping(value = "/rest")
public class SampleController {
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	SampleService sampleService;
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/message/sqs")
	public String testMessageSQS() {
		logger.debug("test message sqs is called");
		//TODO: try catch
		return sampleService.sendMessage();
	}
}
