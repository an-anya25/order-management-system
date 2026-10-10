package com.oms.product_service.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/v1/products").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/v1/products/*").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/products/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/products/*").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
