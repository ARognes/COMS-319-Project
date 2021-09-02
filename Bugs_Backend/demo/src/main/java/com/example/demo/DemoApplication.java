package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties
@EntityScan(basePackages = { "com.example.backend" }) // scan JPA entities
public class DemoApplication {
	private static ConfigurableApplicationContext applicationContext;

	public static void main(String[] args) {
	    DemoApplication.applicationContext = SpringApplication.run(DemoApplication.class, args);
	}

}
