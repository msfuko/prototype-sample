package com.trendmicro.dcs.jiramediator.api.dao;

import com.trendmicro.dcs.jiramediator.api.model.JiraResultBean;
import com.trendmicro.dcs.jiramediator.api.model.rest.AbstractBaseRequest;

public interface BaseRequestDAO {
	
	void put(AbstractBaseRequest request);
	
	JiraResultBean get(AbstractBaseRequest request);
}
