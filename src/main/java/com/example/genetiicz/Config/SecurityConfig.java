package com.example.genetiicz.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() { //this is for example PasswordEncoder passwordEncoder = new PasswordEncoder();
        return new BCryptPasswordEncoder();
    }


    /*
    Later on i need to change the security filter, since i don't want spring to permit after i generate the token
    so i want to get authenticated and check for it. The only thing that it should actually permit is
    the login page and possibility to register
    so it should be then "api/auth/login" and "api/auth/register"
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/user","/api/users/admin", "/api/projects/addproject").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }


    /*@Bean
    public AuthenticationProvider authenticationProvider(HttpSecurity http) throws Exception{
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // making a new instance of the object
        provider.setPasswordEncoder();
    }*/
}
