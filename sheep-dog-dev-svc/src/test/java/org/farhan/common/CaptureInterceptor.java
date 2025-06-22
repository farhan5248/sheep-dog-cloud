package org.farhan.common;

import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CaptureInterceptor implements ClientHttpRequestInterceptor {
    private static int counter = 0;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        // Capture request
        String requestBody = new String(body, StandardCharsets.UTF_8);
        String requestInfo = request.getMethod() + " " + request.getURI() + "\n" + requestBody;
        // Execute and buffer response
        ClientHttpResponse response = execution.execute(request, body);
        byte[] responseBodyBytes = response.getBody().readAllBytes();
        String responseBody = new String(responseBodyBytes, StandardCharsets.UTF_8);
        String responseInfo = response.getStatusCode() + "\n" + responseBody;

        if (!request.getURI().getPath().contains("actuator")) {
            saveGroovyContract("src/test/resources/contracts/" + counter + ".groovy", request, requestBody, response, responseBody);
        }

        // Return a new response with the buffered body
        return new BufferingClientHttpResponseWrapper(response, responseBodyBytes);
    }

    private void saveGroovyContract(String path, HttpRequest request, String requestBody, ClientHttpResponse response, String responseBody) throws IOException {
        String method = request.getMethod().name();
        String url = request.getURI().getPath();
        @SuppressWarnings("deprecation")
        int status = response.getRawStatusCode();
        String contentType = response.getHeaders().getContentType() != null ? response.getHeaders().getContentType().toString() : "application/json";
        StringBuilder groovy = new StringBuilder();
        groovy.append("org.springframework.cloud.contract.spec.Contract.make {\n");
        groovy.append("    request {\n");
        groovy.append("        method '" + method + "'\n");
        groovy.append("        url '" + url + "'\n");
        if (requestBody != null && !requestBody.isBlank()) {
            groovy.append("        body('''" + requestBody.replace("'", "\\'") + "''')\n");
        }
        groovy.append("    }\n");
        groovy.append("    response {\n");
        groovy.append("        status " + status + "\n");
        if (contentType != null) {
            groovy.append("        headers {\n");
            groovy.append("            contentType('" + contentType + "')\n");
            groovy.append("        }\n");
        }
        if (responseBody != null && !responseBody.isBlank()) {
            groovy.append("        body('''" + responseBody.replace("'", "\\'") + "''')\n");
        }
        groovy.append("    }\n");
        groovy.append("}\n");
        String newContract = groovy.toString();

        // Check for duplicate content
        File dir = new File("src/test/resources/contracts");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".groovy"));
        if (files != null) {
            for (File file : files) {
                String existing = new String(java.nio.file.Files.readAllBytes(file.toPath()), java.nio.charset.StandardCharsets.UTF_8);
                if (existing.equals(newContract)) {
                    // Duplicate found, do not save
                    return;
                }
            }
        }
        // No duplicate found, save the contract
        try (FileWriter fw = new FileWriter(path, false)) {
            fw.write(newContract);
            counter++;
        }
    }

    // Wrapper to allow multiple reads of the response body
    private static class BufferingClientHttpResponseWrapper implements ClientHttpResponse {
        private final ClientHttpResponse response;
        private final byte[] body;

        BufferingClientHttpResponseWrapper(ClientHttpResponse response, byte[] body) {
            this.response = response;
            this.body = body;
        }

        @Override
        public InputStream getBody() {
            return new ByteArrayInputStream(body);
        }

        @Override
        public org.springframework.http.HttpHeaders getHeaders() {
            return response.getHeaders();
        }

        @Override
        public org.springframework.http.HttpStatusCode getStatusCode() throws IOException {
            return response.getStatusCode();
        }

        @Override
        public int getRawStatusCode() throws IOException {
            // Deprecated in Spring 6+, but still required for interface
            return response.getRawStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return response.getStatusText();
        }

        @Override
        public void close() {
            response.close();
        }
    }

}
