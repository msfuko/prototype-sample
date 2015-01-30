package com.trendmicro.dcs.springcamelsample.api.service;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trendmicro.dcs.springcamelsample.api.entity.ContentMessage;
import com.trendmicro.dcs.springcamelsample.api.entity.Message;
import com.trendmicro.dcs.springcamelsample.api.utils.messaging.MessageGatewayImpl;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/root-context.xml")
@ActiveProfiles("test")
public class SampleServiceTest {

	@InjectMocks
	private SampleService sampleService;
	
	@Mock
	private MessageGatewayImpl sampleGateway;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSendMessage() {
		ContentMessage message = new ContentMessage();
		message.setTicketNumber("CMDEV-256");
		message.setDescription("Please go smoothly");
		Map<String, String> misc = new HashMap<String, String>();
		misc.put("Dept", "DCS");
		misc.put("Component", "CPU");
		message.setMisc(misc);
		
		assertEquals(sampleService.sendMessage(), message.toString());
	}

}
