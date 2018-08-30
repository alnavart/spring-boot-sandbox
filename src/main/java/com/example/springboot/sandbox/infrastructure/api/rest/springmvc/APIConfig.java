package com.example.springboot.sandbox.infrastructure.api.rest.springmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class APIConfig {

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
    RequestScopeGeneralInfo requestScopeGeneralInfo() {
        return new RequestScopeGeneralInfo();
    }
}
