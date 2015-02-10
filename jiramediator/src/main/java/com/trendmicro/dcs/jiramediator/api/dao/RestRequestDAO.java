package com.trendmicro.dcs.jiramediator.api.dao;

import org.apache.http.auth.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import com.trendmicro.dcs.jiramediator.api.model.JiraResultBean;
import com.trendmicro.dcs.jiramediator.api.model.rest.AbstractBaseRequest;

@Component
public class RestRequestDAO implements BaseRequestDAO {

	@Autowired
	private Credentials credentials;
	
	/**
	 * Wrap http response to ResultBean
	 * @param response
	 * @return result bean
	 */
	private JiraResultBean responseWrapper(ResponseEntity<String> response) {
		JiraResultBean result = new JiraResultBean();
		result.setResponseCode(response.getStatusCode().value());
		result.setResponseContent(response.getBody());
		return result;
	}
	
	public void request(AbstractBaseRequest request) {
		HttpMethod httpMethod = request.getRequestMethod();
		
		switch (httpMethod) {
		
			case PUT: 
				this.put(request);
				break;
				
			case POST:
				this.post(request);
				break;
				
			case GET:
				this.get(request);
				break;
				
			default:
				throw new UnsupportedOperationException();
		}
	}
	
	@Override
	public JiraResultBean get(AbstractBaseRequest request) {
		/*
		 * init a GET request
		 */
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(request.getRequestUri(), String.class);
		return this.responseWrapper(response);
	}
	
	public JiraResultBean post(AbstractBaseRequest request) {
		/*
		 * init a POST request
		 */
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity(
				request.getRequestUri(), request.getPayload(), String.class);
		return this.responseWrapper(response);
	}

	@Override
	public void put(AbstractBaseRequest request) {
		/*
		 * init a PUT request
		 */
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.put(request.getRequestUri(), request.getPayload());
		//TODO: return something
	}
	
	//TODO: maybe in the future we'll need deserialize.. 
	public <T> T get(AbstractBaseRequest request, Class<T> responseType) {
		RestTemplate restTemplate = new RestTemplate();
		T response = restTemplate.getForObject(request.getRequestUri(), responseType);
		return response;
	}

}
