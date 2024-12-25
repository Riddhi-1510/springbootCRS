package com.WoC.SpringBootCRS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@SpringBootApplication
@EnableJpaRepositories
@EnableWebSecurity
public class SpringBootCrsApplication {

	public static void main(String[] args) {
		//return this function: configuration-application-context
		//External_Libraries = node_modules like this
		//pom.xml = package.json like this
		SpringApplication.run(SpringBootCrsApplication.class, args);
	}

}
