package org.tdtu.ecommerceapi.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.tdtu.ecommerceapi.service.LoggingService;

import java.lang.reflect.Type;

@ControllerAdvice
@RequiredArgsConstructor
public class RequestBodyInterceptor extends RequestBodyAdviceAdapter {
    private final LoggingService loggingService;
    private final HttpServletRequest request;

    @Override
    public Object afterBodyRead(
            Object body,
            HttpInputMessage inputMessage,
            MethodParameter parameter,
            Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/v1")) {
            loggingService.logRequest(request, body);
        }
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

    @Override
    public boolean supports(
            MethodParameter methodParameter,
            Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
}
