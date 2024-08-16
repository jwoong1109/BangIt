package com.bangIt.blended;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BangItApplication {

	public static void main(String[] args) {
		SpringApplication.run(BangItApplication.class, args);
	}
	
	@Bean
	 PasswordEncoder passwordEncoder() {
		 return new BCryptPasswordEncoder(14);
	 }
	
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
