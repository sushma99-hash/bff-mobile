package cmu.edu.errors;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

@Component
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        String message = null;
        try {
            if (response.body() != null) {
                message = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            message = "Unable to read response body";
        }
        return new CustomFeignException(response.status(), message);
    }
}
