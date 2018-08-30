package com.example.springboot.sandbox.infrastructure.api.rest.springmvc;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

@Log4j2
public class RequestScopeGeneralInfo implements RequestGeneralInfo {

    public static final String USERNAME_HEADER_LABEL = "From";
    @Autowired
    private HttpServletRequest request;

    @Override
    public String getUserName() {
        String userName = request.getHeader(USERNAME_HEADER_LABEL);
        if (userName == null || userName.isEmpty()) {
            log.warn("Request doesn't contains user");
        }
        return userName;
    }
}
