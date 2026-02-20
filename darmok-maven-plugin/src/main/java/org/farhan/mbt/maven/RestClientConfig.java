package org.farhan.mbt.maven;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class RestClientConfig {

	private int connectTimeout = 60000;
	private int readTimeout = 60000;

	public RestClientConfig() {
	}

	public RestClientConfig(int connectTimeout, int readTimeout) {
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}

	public RestTemplate restTemplate() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(connectTimeout);
		factory.setReadTimeout(readTimeout);
		return new RestTemplate(factory);
	}
}
