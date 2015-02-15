package com.trendmicro.dcs.jiramediator.api.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;  
import static org.mockito.Mockito.verify; 
import static org.mockito.Mockito.when;  
import static org.mockito.Matchers.any;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;

import com.trendmicro.dcs.jiramediator.api.dao.RestRequestDAO;
import com.trendmicro.dcs.jiramediator.api.model.JiraResultBean;
import com.trendmicro.dcs.jiramediator.api.model.rest.issue.IssueRequest;
import com.trendmicro.dcs.jiramediator.api.model.rest.project.ProjectInfoRequest;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/root-context.xml")
@ActiveProfiles("test")
public class JiraIssueServiceTest {

	@Resource
	HttpHost httphost;
	
	@InjectMocks
	@Autowired  
	JiraIssueService jiraIssueService;
	
	//@Spy
	@Mock
	RestRequestDAO restRequestDAO;
	
	@Autowired
	private CacheManager cacheManager;

	private Log logger = LogFactory.getLog(this.getClass());

	private JiraResultBean responseWrapper(ResponseEntity<String> response) {
		JiraResultBean result = new JiraResultBean();
		result.setResponseCode(response.getStatusCode());
		result.setResponseContent(response.getBody());
		return result;
	}
	
	@Before
	public void setUp() throws Exception {
		// init mockito annotations
		MockitoAnnotations.initMocks(this);
		
		// this is the trick for making @Injectmocks and @Autowired work. we need autowired to make @Cacheable work properly. 
		JiraIssueService target = (JiraIssueService) unwrapProxy(jiraIssueService);
		ReflectionTestUtils.setField(target, "restRequestDAO", restRequestDAO); 
	}

	@After
	@CacheEvict(value = "default", allEntries = true)  
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetIssue() {
		IssueRequest request = new IssueRequest(httphost.toURI());
		request.setIssueKey("RFC-50");
		ResponseEntity<String> response = new ResponseEntity<String>(
				this.getGetIssueResponse(), HttpStatus.OK);
		when(restRequestDAO.get(request)).thenReturn(this.responseWrapper(response));
		
		// call once
		JiraResultBean result = jiraIssueService.getIssue(request);
		assertEquals(result.getResponseContent(), this.getGetIssueResponse());
		
		// call again and check restRequestDAO is only called once
		jiraIssueService.getIssue(request);
		verify(restRequestDAO, times(1)).get(request);
	
	}
	
	@Test(expected=RuntimeException.class) 
	public void testGetValueException() {
		IssueRequest request = new IssueRequest(httphost.toURI());
		request.setIssueKey("RFC-199");
		when(restRequestDAO.get(request)).thenThrow(RestClientException.class);
		jiraIssueService.getIssue(request);
	}
	
	@Test
	public void testCreateIssue() throws JsonGenerationException, JsonMappingException, IOException {
		IssueRequest request = new IssueRequest(httphost.toURI());
		request.setPayload(this.getTestIssueRequest());
		ResponseEntity<String> response = new ResponseEntity<String>(
				this.getCreateIssueResponse(), HttpStatus.OK);
		when(restRequestDAO.post(request)).thenReturn(this.responseWrapper(response)); 
		JiraResultBean result = jiraIssueService.syncCreateIssue(request);
		assertEquals(result.getResponseContent(), this.getCreateIssueResponse());
	}

	@Test
	public void testUpdateIssue() throws JsonGenerationException, JsonMappingException, IOException {
		IssueRequest request = new IssueRequest(httphost.toURI());
		request.setIssueKey("RFC-99");
		request.setPayload(this.getTestUpdateIssueRequest());
		ResponseEntity<String> response = new ResponseEntity<String>(
				this.getGetIssueResponse(), HttpStatus.OK);
		when(restRequestDAO.put(request)).thenReturn(this.responseWrapper(response)); 
		JiraResultBean result = jiraIssueService.updateIssue(request);
		assertEquals(result.getResponseContent(), this.getGetIssueResponse());
		
		// check whether CachePut work well so we could get the updated Issue
		assertEquals(result.getResponseContent(), jiraIssueService.getIssue(request).getResponseContent());
	}

	public static final Object unwrapProxy(Object bean) throws Exception {
		/*
		 * If the given object is a proxy, set the return value as the object
		 * being proxied, otherwise return the given object.
		 */
		if (AopUtils.isAopProxy(bean) && bean instanceof Advised) {
			Advised advised = (Advised) bean;
			bean = advised.getTargetSource().getTarget();
		}
		return bean;
	}
	
	private String getTestIssueRequest() throws JsonGenerationException,
	JsonMappingException, IOException {

		Map<String, String> project = new HashMap<String, String>();
		project.put("id", "10260"); 
		Map<String, String> issueType = new HashMap<String, String>();
		issueType.put("id", "35"); 
		Map<String, String> reporter = new HashMap<String, String>();
		reporter.put("name", "otis_lin");
        
        // construct fields property
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("project", project);
        fields.put("issuetype", issueType);
        fields.put("reporter", reporter);
        fields.put("summary", "test summary");
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
	
	private String getCreateIssueResponse() {
		return "{\"id\": \"10000\", \"key\": \"TST-24\",\"self\": "
				+ "\"http://www.example.com/jira/rest/api/2/issue/10000\"}\n";
	}
	
	private String getGetIssueResponse() {
		return "{\"expand\":\"renderedFields,names,schema,transitions,operations,editmeta,changelog\",\"id\":\"197954\",\"se"
				+ "lf\":\"https://dcstaskcentral.trendmicro.com/coc-alpha/rest/api/2/issue/197954\",\"key\":\"RFC-50\",\"fiel"
				+ "ds\":{\"summary\":\"test summary \",\"issuetype\":{\"self\":\"https://dcstaskcentral.trendmicro.com/coc-alp"
				+ "ha/rest/api/2/issuetype/35\",\"id\":\"35\",\"description\":\"RFC for data center infrastructure\",\"iconUr"
				+ "l\":\"https://dcstaskcentral.trendmicro.com/coc-alpha/images/icons/genericissue.gif\",\"name\":\"RFC (Infra"
				+ ")\",\"subtask\":false}}}";
	}
}