package com.trendmicro.dcs.jiramediator.api.utils;

import java.net.URI;

import javax.annotation.Resource;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
@Component
public class HttpComponentsClientHttpRequestFactoryBasicAuth extends HttpComponentsClientHttpRequestFactory {

	@Resource(name = "httphost")
	HttpHost httphost;
	
	@Resource(name = "credentials")
	UsernamePasswordCredentials credentials;

	protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
		return createHttpContext();
	}
	
	private HttpContext createHttpContext() {
		
		// Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(httphost, basicAuth);
        
        // Add AuthCache to the execution context
        HttpClientContext localcontext = HttpClientContext.create();
        localcontext.setAuthCache(authCache);
        
        BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(httphost), credentials);
        localcontext.setCredentialsProvider(credsProvider);
        return localcontext;
        
  }
}