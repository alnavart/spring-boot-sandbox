package com.example.springboot.sandbox.infrastructure.api.rest.springmvc;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class NullRequestScopeGeneralInfo implements RequestGeneralInfo {

    @Override
    public String getUserName() {
        log.warn("Calling null request general info");
        return "";
    }
}
