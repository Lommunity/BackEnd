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
        // ------------------------ 위에건 일단 무시 ------------------------

        // 1. admin/test api를 호출한다.
        // 2. JwtAuthenticationFilter에서 authentication을 SecurityContext에 추가한다.
        // 3. spring security는 SecurityContext에서 추가된 authentication를 가져와 권한을 확인한다.
        // 4. 권한이 맞으면 api가 수행. 권한이 맞지 않으면 api가 실패.
        http.authorizeRequests()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/user/**").hasRole("USER")
            .antMatchers("/post/**").authenticated()
            .anyRequest().permitAll();

        http.addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationProvider, authenticationExceptionHandler), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
