package com.trendmicro.dcs.springcamelsample.api.service;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trendmicro.dcs.springcamelsample.api.entity.ContentMessage;
import com.trendmicro.dcs.springcamelsample.api.entity.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/root-context.xml")
@ActiveProfiles("test")
public class JmsDemoServiceTest {

	@Autowired
	private JmsDemoService jmsDemoService;

	private Message getDummyMessage(){
		ContentMessage message = new ContentMessage();
		message.setTicketNumber("CMDEV-512");
		message.setDescription("Yeah, looks like so :-)");
		Map<String, String> misc = new HashMap<String, String>();
		misc.put("Dept", "DCS-RD");
		misc.put("Component", "Keyboard");
		message.setMisc(misc);
		
		return message;
	}
	
	@Test
	public void testGetJIRATickets() {
		Message result;
		
		result = jmsDemoService.getJIRATickets(this.getDummyMessage());
		assertEquals("ok", result.getDescription());
	}

}
