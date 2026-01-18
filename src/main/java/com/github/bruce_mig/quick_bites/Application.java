package com.github.bruce_mig.quick_bites;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
@EnableResilientMethods
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
