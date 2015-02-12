package com.trendmicro.dcs.jiramediator.api.itest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.HttpHost;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trendmicro.dcs.jiramediator.api.model.JiraResultBean;
import com.trendmicro.dcs.jiramediator.api.model.rest.function.SearchRequest;
import com.trendmicro.dcs.jiramediator.api.model.rest.issue.IssueRequest;
import com.trendmicro.dcs.jiramediator.api.service.JiraIssueService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/root-context.xml")
@ActiveProfiles("test")
public class JiraIssueServiceITest {

	@Autowired
	JiraIssueService jiraIssueService;
	
	@Resource
	HttpHost httphost;

	private Date testDate;
	
	@Before
	public void setUp() throws Exception {
		testDate = new Date();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSyncCreateIssue() throws JsonGenerationException, JsonMappingException, IOException {
		IssueRequest request = new IssueRequest(httphost.toURI());
		request.setPayload(this.getTestIssueRequest());
		jiraIssueService.syncCreateIssue(request);
	}
	
	@Test
	public void searchIssue() throws JsonGenerationException, JsonMappingException, IOException {
		SearchRequest request = new SearchRequest(httphost.toURI());
		String jql = "created > startOfDay(-0d) and reporter in (chloe_lee)";
		request.setJql(jql);
		request.setMaxResults(20);
		request.setStartAt(0);
		request.setFields(Arrays.asList("summary", "status"));
		JiraResultBean resultBean = jiraIssueService.searchIssue(request);
		assertEquals(HttpStatus.OK, resultBean.getResponseCode());
	}
	
	private boolean existIssue(String identifier) throws JsonGenerationException, JsonMappingException, IOException {
		SearchRequest request = new SearchRequest(httphost.toURI());
		String jql = "created > startOfDay(-0d) and reporter in (chloe_lee)";
		request.setJql(jql);
		request.setMaxResults(20);
		request.setStartAt(0);
		request.setFields(Arrays.asList("summary", "status"));
		JiraResultBean resultBean = jiraIssueService.searchIssue(request);
		if (resultBean.getResponseContent().toString().indexOf(identifier) > -1) {
			return true;
		}
		return false;
	}

	@Test(timeout = 60000)
	public void testCreateIssue() throws JsonGenerationException, JsonMappingException, IOException, InterruptedException {
		IssueRequest request = new IssueRequest(httphost.toURI());
		request.setPayload(this.getTestIssueRequest());
		jiraIssueService.createIssue(request);
		Thread.sleep(10000);
		assertTrue(this.existIssue(testDate.toString()));
	}
	
	@Test
	public void testGetIssue() {
		IssueRequest request = new IssueRequest(httphost.toURI());
		request.setIssueKey("RFC-100");
		JiraResultBean result = jiraIssueService.getIssue(request);
		assertEquals(HttpStatus.OK, result.getResponseCode());
	}
	
	@Test
	public void testUpdateIssue() throws JsonGenerationException, JsonMappingException, IOException {
		IssueRequest request = new IssueRequest(httphost.toURI());
		request.setIssueKey("RFC-189");
		request.setPayload(getTestUpdateIssueRequest());
		JiraResultBean result = jiraIssueService.updateIssue(request);
		assertEquals(HttpStatus.OK, result.getResponseCode());
	}
	
	private String getTestIssueRequest() throws JsonGenerationException,
	JsonMappingException, IOException {

		Map<String, String> project = new HashMap<String, String>();
		project.put("id", "10260"); 
		Map<String, String> issueType = new HashMap<String, String>();
		issueType.put("id", "35"); 
		Map<String, String> reporter = new HashMap<String, String>();
		reporter.put("name", "chloe_lee");
        
        // construct fields property
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("project", project);
        fields.put("issuetype", issueType);
        fields.put("reporter", reporter);
        fields.put("summary", "test summary " + testDate);
        fields.put("description", "test desc");
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("fields", fields);
        
        return new ObjectMapper().writeValueAsString(reqMap);
	}
	
	private String getTestUpdateIssueRequest() throws JsonGenerationException, 
	JsonMappingException, IOException {
		// construct fields property
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("summary", "update issue summary, time" + new Date());
		fields.put("description", "update issue desc, time" + new Date());
				
		// construct the requestMap, then construct a json object by it 
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("fields", fields);
		
		return new ObjectMapper().writeValueAsString(reqMap);
	}

}
