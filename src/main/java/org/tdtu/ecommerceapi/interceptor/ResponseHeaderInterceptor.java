package org.tdtu.ecommerceapi.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ResponseHeaderInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        response.setHeader(HttpHeaders.CACHE_CONTROL, CacheControl.noCache().getHeaderValue());
        response.setHeader(HttpHeaders.PRAGMA, CacheControl.noCache().getHeaderValue());
        response.setHeader(HttpHeaders.EXPIRES, "0");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
        // TODO: Add Default Response Header here
        return true;
    }
}
