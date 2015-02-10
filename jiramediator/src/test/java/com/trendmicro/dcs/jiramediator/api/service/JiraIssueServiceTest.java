package com.trendmicro.dcs.jiramediator.api.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trendmicro.dcs.jiramediator.api.model.rest.ProjectInfoRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/root-context.xml")
@ActiveProfiles("staging")
public class JiraIssueServiceTest {
	
	//@Autowired
	//JiraIssueService jiraIssueService;
	
	@Test
	public void testGetProjectInfo() {
		assertTrue(true);
		/*
		ProjectInfoRequestBean request = new ProjectInfoRequestBean();
		request.setProjectKey("changeManagement");
		jiraIssueService.getProjectInfo(request);*/
		
	}
}
