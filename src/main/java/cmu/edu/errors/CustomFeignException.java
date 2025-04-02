package cmu.edu.errors;//package cmu.edu.ds.errors;

import feign.Response;
import feign.Util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

//public class CustomFeignException extends RuntimeException {
//    private final int status;
//    private final Response.Body body;
//
//    public CustomFeignException(int status, Response.Body body) {
//        super("Feign client error: " + status);
//        this.status = status;
//        this.body = body;
//    }
//
//    public int getStatus() {
//        return status;
//    }
//
//    public Response.Body getBody() {
//        return body;
//    }
//}

public class CustomFeignException extends RuntimeException {
    private final int status;
    private final String responseBody;

    public CustomFeignException(int status, Response.Body body) {
        super("Feign client error: " + status);
        this.status = status;
        this.responseBody = parseBody(body); // Read body immediately
    }

    private String parseBody(Response.Body body) {
        try {
            return body == null ? null : Util.toString(body.asReader());
        } catch (IOException e) {
            return "Unable to read response body: " + e.getMessage();
        }
    }

    // Getters
    public int getStatus() { return status; }
    public InputStream getResponseBody() {
//        return responseBody;
        return new ByteArrayInputStream(responseBody.getBytes(StandardCharsets.UTF_8));
    }
}