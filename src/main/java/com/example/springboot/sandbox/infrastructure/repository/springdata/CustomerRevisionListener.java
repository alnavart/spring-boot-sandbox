package com.example.springboot.sandbox.infrastructure.repository.springdata;

import com.example.springboot.sandbox.commons.Commons;
import org.hibernate.envers.RevisionListener;

public class CustomerRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		CustomerRevEntity exampleRevEntity = (CustomerRevEntity) revisionEntity;
		exampleRevEntity.setUsername(Commons.USER.get());
	}

}
