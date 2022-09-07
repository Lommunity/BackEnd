package com.Lommunity.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.formLogin().disable();
        http.httpBasic().disable();
        http.cors(cors -> cors.disable());

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.exceptionHandling()
            .authenticationEntryPoint(authenticationExceptionHandler);

        http.authorizeRequests()
            .antMatchers("/auth/login").permitAll()
            .anyRequest().authenticated();

        http.addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationProvider, authenticationExceptionHandler), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
