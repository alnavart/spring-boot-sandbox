package com.example.springboot.sandbox.infrastructure.repository.springdata;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Entity;

@Entity
@RevisionEntity(CustomerRevisionListener.class)
public class CustomRevisionEntity extends DefaultRevisionEntity {

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
