package cmu.edu.errors;//package cmu.edu.ds.errors;//package cmu.edu.ds.errors;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
//public class FeignErrorDecoder implements ErrorDecoder {
//
//    @Override
//    public Exception decode(String methodKey, Response response) {
//        return new CustomFeignException(response.status(), response.body());
//    }
//}

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            // Read the response body immediately (can only be read once)
            String body = response.body() != null ?
                    Util.toString(response.body().asReader()) :
                    "{\"error\":\"No error details provided\"}";

            return new CustomFeignException(response.status(), response.body());
        } catch (IOException e) {
            // If we can't read the body, create an exception with just the status
            return new CustomFeignException(response.status(), null);
        }
    }
}