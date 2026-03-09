package com.neuracare.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NeuracareBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeuracareBackendApplication.class, args);
	}

}
