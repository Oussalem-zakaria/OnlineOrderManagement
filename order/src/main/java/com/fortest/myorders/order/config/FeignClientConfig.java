package com.fortest.myorders.order.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                    .getAuthentication();

            if (authentication != null && authentication.getToken() != null) {
                String tokenValue = authentication.getToken().getTokenValue();
                requestTemplate.header("Authorization", "Bearer " + tokenValue);
            }
        };
    }
}
