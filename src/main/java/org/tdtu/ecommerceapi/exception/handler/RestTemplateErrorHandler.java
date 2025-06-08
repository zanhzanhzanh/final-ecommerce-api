package org.tdtu.ecommerceapi.exception.handler;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.tdtu.ecommerceapi.exception.ServiceUnavailableException;
import org.tdtu.ecommerceapi.exception.UnauthorizedException;

import java.io.IOException;
import java.net.URI;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class RestTemplateErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError()
                || response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is5xxServerError()) {
            if (response.getStatusCode().equals(SERVICE_UNAVAILABLE)) {
                throw new ServiceUnavailableException(response.getBody().toString());
            }
            throw new RuntimeException(response.getBody().toString());
        } else {
            if (response.getStatusCode().equals(UNAUTHORIZED)) {
                throw new UnauthorizedException();
            }
            throw new RuntimeException(response.getBody().toString());
        }
    }
}
