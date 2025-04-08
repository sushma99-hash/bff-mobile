package cmu.edu.errors;

public class CustomFeignException extends RuntimeException {
    private final int status;
    private final String responseBody;

    public CustomFeignException(int status, String responseBody) {
        super("Feign client error: " + status);
        this.status = status;
        this.responseBody = responseBody;
    }

    public int getStatus() {
        return status;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
