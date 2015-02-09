package com.trendmicro.dcs.jiramediator.api.dao;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.trendmicro.dcs.jiramediator.api.model.rest.AbstractBaseRequestBean;

@Component
public class RestRequestDAO implements BaseRequestDAO {

	@Override
	public void put() {
		
	}

	@Override
	public void get() {
		
	}
	
	
	public <T> T get(AbstractBaseRequestBean request, Class<T> responseType) {
		RestTemplate restTemplate = new RestTemplate();
		T response = restTemplate.getForObject(request.getRequestUrl(), responseType);
		return response;
	}

}
