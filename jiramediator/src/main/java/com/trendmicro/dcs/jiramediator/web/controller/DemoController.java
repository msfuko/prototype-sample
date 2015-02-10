package com.trendmicro.dcs.jiramediator.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trendmicro.dcs.jiramediator.api.model.JiraResultBean;
import com.trendmicro.dcs.jiramediator.api.model.rest.IssueRequest;
import com.trendmicro.dcs.jiramediator.api.model.rest.ProjectInfoRequest;
import com.trendmicro.dcs.jiramediator.api.service.JiraIssueService;

@Controller
@RequestMapping(value = "/rest")
public class DemoController {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	JiraIssueService jiraIssueService;
	
	@Autowired
	HttpHost httphost;
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/demo/get")
	public String testGetRequest() {
		logger.info("test is called");
		ProjectInfoRequest request = new ProjectInfoRequest(httphost.toURI());
		request.setProjectKey("RFC");
		JiraResultBean result = jiraIssueService.getProjectInfo(request);
		return result.getResponseContent().toString();
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/demo/post")
	public String testPostRequest() throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("test is called");
		IssueRequest request = new IssueRequest(httphost.toURI(), this.getSampleIssueRequest());
		request.setIssueKey("TEST-1");
		jiraIssueService.createIssue(request);
		return "Request sent";
	}

	private String getSampleIssueRequest() throws JsonGenerationException, 
	JsonMappingException, IOException {
        Map<String, String> project = new HashMap<String, String>();
        project.put("id", "10260"); 
        Map<String, String> issueType = new HashMap<String, String>();
        issueType.put("id", "35"); 
        Map<String, String> reporter = new HashMap<String, String>();
        reporter.put("name", "test");
        
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
}
