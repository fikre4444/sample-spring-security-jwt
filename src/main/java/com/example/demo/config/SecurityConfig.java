package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.filter.JwtFilter;
import com.example.demo.service.MyUserDetailsService;

@Configuration
public class SecurityConfig {

  @Autowired
  private MyUserDetailsService userDetailsService;

  // this is the jwt filter used for jwt authentication and authorization
  @Autowired
  private JwtFilter jwtFilter;

  // this is where we define our security filter chain which includes a jwt filter
  // and authorization rules with multiple roles.
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http = http.csrf(customzer -> customzer.disable())
        .httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(customizer -> customizer
            // the api user endpoints are permitted for all
            .requestMatchers("/api/user/register", "/api/user/login").permitAll()
            // the /hello endpoint is used for admin only
            .requestMatchers("/hello").hasRole("ADMIN")
            // the /bye endpoint is used for user only
            .requestMatchers("/bye").hasRole("USER")
            // the /staff endpoint is used for staff only
            .requestMatchers("/staff").hasRole("STAFF"))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

  // this is the authentication provider that is used when a user tries to login
  // it is also used in the login service created here.
  @Bean
  public AuthenticationProvider authenticator() {
    DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
    dao.setUserDetailsService(userDetailsService);
    dao.setPasswordEncoder(new BCryptPasswordEncoder());

    return dao;
  }

  // The password encoder used by the AuthenticationProvider above
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // We use this to have access to the authentication manager from our code base
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

}
