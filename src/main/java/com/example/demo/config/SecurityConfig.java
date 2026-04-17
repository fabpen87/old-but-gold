package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring Security configuration using the classic (pre-Spring Security 5.7)
 * {@link WebSecurityConfigurerAdapter} style. This is deprecated from Spring
 * Security 5.7 and <b>removed</b> in Spring Security 6 / Spring Boot 3.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/greet").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }
}
