package com.trendmicro.dcs.springcamelsample.api.service;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trendmicro.dcs.springcamelsample.api.dao.JmsResponseDAO;
import com.trendmicro.dcs.springcamelsample.api.entity.ContentMessage;
import com.trendmicro.dcs.springcamelsample.api.entity.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/root-context.xml")
@ActiveProfiles("test")
public class JmsDemoServiceTest {

	@Autowired
	private JmsDemoService jmsDemoService;
	
	@Autowired
	private JmsResponseDAO jmsResponseDAO;

	private Message getDummyMessage(String ticketId){
		ContentMessage message = new ContentMessage();
		message.setTicketNumber(ticketId);
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
		String ticketId = "CMDEV-123";
		
		result = jmsDemoService.getJIRATickets(this.getDummyMessage(ticketId));
		//must match ticketId
		assertEquals(ticketId, result.getTicketNumber());
	}

	@Test
	public void testGetJIRATicketsWithDummyEntities(){
		Message message;
		String correlationId;
		String ticketId;
		Random r = new Random();
		
		
		//prepare dummy entities
		for (int i=0; i<20; i++){
			message = this.getDummyMessage("CMDEV-1" + Math.abs(r.nextInt()));
			correlationId = UUID.randomUUID().toString();
			jmsResponseDAO.enqueue(message, "dummyId", correlationId);
		}
		
		//submit entity
		ticketId = "CMDEV-234";
		message = jmsDemoService.getJIRATickets(this.getDummyMessage(ticketId));
		//should receive CMDEV-234 response
		assertEquals(ticketId, message.getTicketNumber());
	}
}
