package com.trendmicro.dcs.jiramediator.api.model.rest.function;

import java.util.List;

import org.springframework.web.util.UriComponentsBuilder;

import com.trendmicro.dcs.jiramediator.api.model.rest.AbstractBaseRequest;

public class SearchRequest extends AbstractBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7449575313186973302L;

	public SearchRequest(String httpHost) {
		super(httpHost);
	}
	
	private String jql;
	private int startAt;
	private int maxResults;
	private List<String> fields;

	public String getJql() {
		return jql;
	}

	public void setJql(String jql) {
		this.jql = jql;
	}

	public int getStartAt() {
		return startAt;
	}

	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	@Override
	public void requestUriInit() {
		switch (this.requestMethod) {
		case POST:
			requestUri = UriComponentsBuilder.fromUriString(getHttpHost())
			.path("/search").build().toUri();
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

}
