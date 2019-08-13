package com.rabobank.customer.statementsprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * This is the Spring Boot Application where Rabo Bank's monthly customer deliveries are validated.
 * The customer as of now can upload xml and csv formats.  The application can be extended to other formats by adding new services.
 */

@SpringBootApplication
public class StatementsApplication  extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(StatementsApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(StatementsApplication.class, args);
	}

}
