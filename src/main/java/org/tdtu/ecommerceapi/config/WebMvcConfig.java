package org.tdtu.ecommerceapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.tdtu.ecommerceapi.converter.AclConverter;
import org.tdtu.ecommerceapi.converter.ContentDispositionConverter;
import org.tdtu.ecommerceapi.interceptor.LoggingInterceptor;
import org.tdtu.ecommerceapi.interceptor.ResponseHeaderInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final LoggingInterceptor loggingInterceptor;
    private final ResponseHeaderInterceptor responseHeaderInterceptor;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", HandlerTypePredicate.forAnnotation(RestController.class));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor).addPathPatterns("/api/v1/**").order(1);
        registry.addInterceptor(responseHeaderInterceptor).addPathPatterns("/api/v1/**").order(2);
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ContentDispositionConverter());
        registry.addConverter(new AclConverter());
        WebMvcConfigurer.super.addFormatters(registry);
    }
}
