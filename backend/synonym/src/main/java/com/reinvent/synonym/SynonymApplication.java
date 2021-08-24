package com.reinvent.synonym;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SynonymApplication {

	//Model mapper registered as a bean and autowired in the controller to map entities to/from DTOs
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SynonymApplication.class, args);
	}

}
