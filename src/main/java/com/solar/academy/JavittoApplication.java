package com.solar.academy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


@EnableMethodSecurity
@SpringBootApplication
public class JavittoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavittoApplication.class, args);

	}
}
