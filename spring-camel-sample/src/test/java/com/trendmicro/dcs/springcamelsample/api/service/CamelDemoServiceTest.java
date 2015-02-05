package com.trendmicro.dcs.springcamelsample.api.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trendmicro.dcs.springcamelsample.api.entity.Message;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/root-context.xml")
@ActiveProfiles("staging")
public class CamelDemoServiceTest {

	@Autowired
	SampleService sampleService;
	
	@Test
	public void test() throws InterruptedException {
		Message message = sampleService.sendMessage();
		assertNotNull(message);
		assertEquals(message.getTicketNumber(), "CMDEV-256");
		//Thread.sleep(3000);
	}

}
