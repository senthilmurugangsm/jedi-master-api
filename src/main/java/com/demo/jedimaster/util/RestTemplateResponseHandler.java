package com.demo.jedimaster.util;

import com.demo.jedimaster.exception.RestTemplateException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class RestTemplateResponseHandler {

    public <T> T handleResponse(ResponseEntity<T> responseEntity) throws RestTemplateException {
        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            return responseEntity.getBody();
        } else {
            String errorMessage = "Error occurred while calling Swapi API. Status code: " + responseEntity.getStatusCodeValue();
            if (responseEntity.getBody() instanceof String) {
                errorMessage += ". Error message: " + responseEntity.getBody();
            }
            throw new RestTemplateException(errorMessage, responseEntity.getStatusCode());
        }
    }
}