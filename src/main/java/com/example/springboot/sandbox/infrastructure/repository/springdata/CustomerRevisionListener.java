package com.example.springboot.sandbox.infrastructure.repository.springdata;

import com.example.springboot.sandbox.infrastructure.api.rest.springmvc.RequestGeneralInfo;
import com.example.springboot.sandbox.infrastructure.cdi.spring.ContextLookup;
import lombok.extern.log4j.Log4j2;
import org.hibernate.envers.RevisionListener;

@Log4j2
class CustomerRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity exampleRevEntity = (CustomRevisionEntity) revisionEntity;
        RequestGeneralInfo requestGeneralInfo = ContextLookup.requestScopeGeneralInfo();
        exampleRevEntity.setUsername(requestGeneralInfo.getUserName());
    }

}
