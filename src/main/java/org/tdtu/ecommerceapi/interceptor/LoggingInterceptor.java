package org.tdtu.ecommerceapi.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.tdtu.ecommerceapi.service.LoggingService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {
    private final LoggingService loggingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        List<String> methodList = new ArrayList<>();
        methodList.add(HttpMethod.GET.name());
        methodList.add(HttpMethod.DELETE.name());
        if (methodList.contains(request.getMethod())) {
            // For requests that won't go through RequestBodyInterceptor
            loggingService.logRequest(request, null);
        }
        return true;
    }
}
