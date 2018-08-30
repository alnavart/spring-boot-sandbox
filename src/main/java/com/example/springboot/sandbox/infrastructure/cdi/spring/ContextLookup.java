package com.example.springboot.sandbox.infrastructure.cdi.spring;

import com.example.springboot.sandbox.infrastructure.api.rest.springmvc.NullRequestScopeGeneralInfo;
import com.example.springboot.sandbox.infrastructure.api.rest.springmvc.RequestGeneralInfo;
import com.example.springboot.sandbox.infrastructure.api.rest.springmvc.RequestScopeGeneralInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Log4j2
@Component
public class ContextLookup implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static RequestGeneralInfo requestScopeGeneralInfo() {
        RequestGeneralInfo requestScopeGeneralInfo = new NullRequestScopeGeneralInfo();
        if (context == null) {
            log.warn("No application context available");
        } else {
            if (RequestContextHolder.getRequestAttributes() == null) {
                log.warn("No request context available");
            } else {
                requestScopeGeneralInfo = context.getBean(RequestScopeGeneralInfo.class);
            }
        }

        return requestScopeGeneralInfo;
    }

}
