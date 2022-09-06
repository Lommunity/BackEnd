package com.Lommunity;

import com.Lommunity.infrastructure.UserAuditorAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class) // default user를 생성하지 않도록 한다.
public class LommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(LommunityApplication.class, args);
	}

	@Bean
	public AuditorAware<Long> userAuditorAware() {
		return new UserAuditorAware();
	}

}
