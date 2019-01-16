package com.janhen.seckill;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class MainApplication_War extends SpringBootServletInitializer{

	/*public static void main(String[] args) {
		SpringApplication.run(MainApplication_War.class, args);
	}*/
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		
		return builder.sources(MainApplication_War.class);
	}
	

}
