package com.trendmicro.dcs.jiramediator.api.dao;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHost;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;

import com.trendmicro.dcs.jiramediator.api.model.JiraResultBean;
import com.trendmicro.dcs.jiramediator.api.model.rest.AbstractBaseRequest;


@Component
public class RestRequestDAO implements BaseRequestDAO {

	private static final String JIRA_TOKEN_HEADER = "X-Atlassian-Token";
	
	@Resource(name = "httphost")
	HttpHost httphost;
	
	@Resource(name = "username")
	private String username;
	
	@Resource(name = "password")
	private String password;
	
	/**
	 * Wrap http response to ResultBean
	 * @param response
	 * @return result bean
	 */
	public JiraResultBean responseWrapper(ResponseEntity<String> response) {
		JiraResultBean result = new JiraResultBean();
		result.setResponseCode(response.getStatusCode());
		result.setResponseContent(response.getBody());
		return result;
	}
	
	public JiraResultBean request(AbstractBaseRequest request) {
		HttpMethod httpMethod = request.getRequestMethod();
		JiraResultBean result = null;
		switch (httpMethod) {
		
			case PUT: 
				result = this.put(request);
				break;
				
			case POST:
				result = this.post(request);
				break;
				
			case GET:
				result = this.get(request);
				break;
				
			default:
				throw new UnsupportedOperationException();
		}
		return result;
	}
	
	@Override
	public JiraResultBean get(AbstractBaseRequest request) {
		/*
		 * init a GET request
		 */
		RestTemplate restTemplate = new RestTemplate();
		request.requestUriInit();
		ResponseEntity<String> response = restTemplate.getForEntity(request.getRequestUri(), String.class);
		return this.responseWrapper(response);
	}

	private String getJiraAuthHeader() {
        StringBuffer stringBuffer = new StringBuffer().append(this.username)
        		.append(":").append(this.password);
        byte[] encodedBytes = Base64.encodeBase64(stringBuffer.toString().getBytes());
        
        return "Basic ".concat(new String(encodedBytes));
    }

	private HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.add(JIRA_TOKEN_HEADER, "nocheck");
		headers.add(HttpHeaders.AUTHORIZATION, this.getJiraAuthHeader());
		return headers;
	}
	
	public JiraResultBean post(AbstractBaseRequest request) {
		/*
		 * init a POST request
		 */
		RestTemplate restTemplate = new RestTemplate();
		request.requestUriInit();
		HttpEntity<String> httpRequest = new HttpEntity<String>(request.getPayload(), this.getHttpHeaders());
		ResponseEntity<String> response = restTemplate.postForEntity(request.getRequestUri(), 
				httpRequest, String.class);
		return this.responseWrapper(response);
	}

	@Override
	public JiraResultBean put(AbstractBaseRequest request) {
		/*
		 * init a PUT request
		 */
		RestTemplate restTemplate = new RestTemplate();
		request.requestUriInit();
		HttpEntity<String> httpRequest = new HttpEntity<String>(request.getPayload(), getHttpHeaders());
		ResponseEntity<String> response = 
				restTemplate.exchange(request.getRequestUri().toString(), 
						HttpMethod.GET, httpRequest, String.class);
		
		return this.responseWrapper(response);
	}
	
	
	//TODO: maybe in the future we'll need deserialize.. 
	public <T> T get(AbstractBaseRequest request, Class<T> responseType) {
		RestTemplate restTemplate = new RestTemplate();
		request.requestUriInit();
		T response = restTemplate.getForObject(request.getRequestUri(), responseType);
		return response;
	}

}
