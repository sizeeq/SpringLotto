package com.gmail.sizeeq.lotto.infrastructure.numbergenerator.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        final HttpStatusCode statusCode = httpResponse.getStatusCode();
        if (statusCode.is5xxServerError()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while using http client");
        } else if (statusCode.is4xxClientError()) {
            if (statusCode == NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            } else if (statusCode == UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }
    }
}
