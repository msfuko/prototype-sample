package com.trendmicro.dcs.springcamelsample.api.utils.messaging;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trendmicro.dcs.springcamelsample.api.entity.ContentMessage;
import com.trendmicro.dcs.springcamelsample.api.entity.Message;
import com.trendmicro.dcs.springcamelsample.api.utils.messaging.routes.ProducerRouteBuilder;

//TODO - make it perfect with spring ...

//@ActiveProfiles("test")
public class RouteBuilderTest extends CamelTestSupport{

	 @EndpointInject(uri = "mock:demo")
	 protected MockEndpoint resultEndpoint;
	 
	 @Produce(uri = "seda:outboundMessageChannel")
	 protected ProducerTemplate template;
	 
	 MessageGatewayImpl messageGateway = new MessageGatewayImpl();
	 
	 //@Autowired
	 //ProducerRouteBuilder routeBuilder;
	 
	 private Message getTestMessage() {
			ContentMessage message = new ContentMessage();
			message.setTicketNumber("CMDEV-256");
			message.setDescription("Please go smoothly");
			Map<String, String> misc = new HashMap<String, String>();
			misc.put("Dept", "DCS");
			misc.put("Component", "CPU");
			message.setMisc(misc);
			return message;
	 }
	 
	 @Test
	 public void testSendMatchingMessage() throws Exception {
	     resultEndpoint.expectedBodiesReceived(this.getTestMessage().toString());
	 
	     template.sendBody(this.getTestMessage().toString());
	 
	     resultEndpoint.assertIsSatisfied();
	 }
	 
	 @Test
	 public void testSendNotMatchingMessage() throws Exception {
	     resultEndpoint.expectedMessageCount(1);
	     
	     template.sendBody(this.getTestMessage().toString());
	 
	     resultEndpoint.assertIsSatisfied();
	 }
	    
	 @Override
	 protected RouteBuilder createRouteBuilder() {
		 return new RouteBuilder() {
			 public void configure() {
				 from("seda:outboundMessageChannel")
				 .log(LoggingLevel.INFO, "Message starts to send")
				 .errorHandler(deadLetterChannel("log:failedLetter"))
				 .to("mock:demo");
			 }
		};
	}

}
