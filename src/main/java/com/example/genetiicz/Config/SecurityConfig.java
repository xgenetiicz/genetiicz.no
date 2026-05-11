package com.example.genetiicz.Config;

import com.example.genetiicz.DTO.AuthRequestDTO;
import com.example.genetiicz.Filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;

    }


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
                )
                    .httpBasic(Customizer.withDefaults())
                    .formLogin(Customizer.withDefaults()); //fetched from https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html#servlet-authentication-unpwd
        return http.build();
    }

    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(request -> request.anyRequest().permitAll()); //all requests should be authenticated.
        http.formLogin(Customizer.withDefaults());//fetched from https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html#servlet-authentication-unpwd
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }/*






    //@Bean
    //public AuthenticationManager authenticationManager (UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception{
        /* based on: TODO: https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html
                     TODO: https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html
        AuthenticationProvider is within AuthenticationManager, and this should call on DaoProvider, who will call on UserDetailsService
        and PasswordEncoder(passwordEncoder) that is BCrypt.
        */
      //  DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService()); // DaoAuthenticationProvider uses the passwordEncoder.
        //authenticationProvider.setPasswordEncoder(passwordEncoder);
        //TODO: authenticationProvider.s

    // return new ProviderManager(authenticationProvider);
    // }

   /* @Bean
    public UserDetailsService userDetailsService() {
      //TODO: UserDetailsService userDetails = new User.
    }*/




}
