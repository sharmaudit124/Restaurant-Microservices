package com.restaurant.chefservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ChefServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChefServiceApplication.class, args);
	}

}
