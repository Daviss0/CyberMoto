package com.cybermoto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CybermotoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CybermotoApplication.class, args);
		System.out.println("running");

	}

}
