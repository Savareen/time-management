package com.igorzaitcev.management;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.igorzaitcev.management.utils.DBRegistrator;

@SpringBootApplication
public class ManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementApplication.class, args);
	}

	@Profile("!test")
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> DBRegistrator.populateDB();

	}



}
