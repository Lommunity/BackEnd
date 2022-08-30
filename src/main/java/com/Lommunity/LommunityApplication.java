package com.Lommunity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class) // default user를 생성하지 않도록 한다.
public class LommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(LommunityApplication.class, args);
	}

}
