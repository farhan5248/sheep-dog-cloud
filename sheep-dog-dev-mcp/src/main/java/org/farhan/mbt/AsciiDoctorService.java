/*
* Copyright 2024 - 2024 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.farhan.mbt;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class AsciiDoctorService {

	Logger logger = LoggerFactory.getLogger(AsciiDoctorService.class);
	private static String BASE_URL;
	private final int RETRY_COUNT = 10;
	private final RestClient restClient;

	@Autowired
	public AsciiDoctorService(@Value("${sheepdog.host:sheep-dog-dev-svc}") String host,
			@Value("${sheepdog.port:80}") int port) {
		// TODO look into why using @ConfigurationProperties creates issues.
		// TODO why does this service need a controller?

		BASE_URL = "http://" + host + ":" + port;
		this.restClient = RestClient.builder()
				.baseUrl(BASE_URL)
				.build();
	}

	/**
	 * Clear objects for AsciiDoctor files
	 * 
	 * @param tags Test case tags, can be blank or empty
	 * @throws RestClientException if the request fails
	 * @throws Exception           if the maximum number of retries is reached
	 */
	@Tool(name = "Clear objects for AsciiDoctor files", description = "Clear objects for AsciiDoctor files in the UML model")
	public void clearConvertAsciidoctorToUMLObjects(String tags) throws Exception {
		logger.info("Clearing objects for AsciiDoctor files with tags: " + tags);
		TreeMap<String, String> parameters = new TreeMap<String, String>();
		String uri = "/asciidoctor/clearConvertAsciidoctorToUMLObjects";
		if (!tags.isEmpty()) {
			parameters.put("tags", tags);
			uri += "?tags={tags}";
		}
		int retryCount = 0;
		while (retryCount < RETRY_COUNT) {
			try {
				restClient.delete().uri(uri, parameters).retrieve().body(Void.class);
				return; // Exit if successful
			} catch (Exception e) {
				logger.error("Retry attempt " + (retryCount + 1), e);
				Thread.sleep(1000);
				retryCount++;
			}
		}
		throw new Exception("Max retries reached while clearing objects for AsciiDoctor files");
	}

}