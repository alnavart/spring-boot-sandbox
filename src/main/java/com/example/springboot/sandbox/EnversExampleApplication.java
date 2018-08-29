package com.example.springboot.sandbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public class EnversExampleApplication {

	private static final Logger log = LoggerFactory.getLogger(EnversExampleApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EnversExampleApplication.class, args);
		log.info("Envers app is running");
	}
}
