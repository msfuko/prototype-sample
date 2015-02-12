package com.trendmicro.dcs.jiramediator.api.dao;

import com.trendmicro.dcs.jiramediator.api.model.rest.AbstractBaseRequest;

public interface BaseRequestDAO {
	
	Object put(AbstractBaseRequest request);
	
	Object get(AbstractBaseRequest request);
}
