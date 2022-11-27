package de.tigges.eventmanagement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin().and()
                .authorizeHttpRequests().and()
                .authorizeHttpRequests().anyRequest().authenticated().and()
                .csrf().disable()
                .build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
                .username("dacapo")
                .password("{bcrypt}$2a$10$Cf5rOfI9sqO7nnYKg8Y8JeeBvxn00X.TSYZw3VdP5OfZ6J0X6H3KC")
                .roles("USER").build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{bcrypt}$2a$10$Cf5rOfI9sqO7nnYKg8Y8JeeBvxn00X.TSYZw3VdP5OfZ6J0X6H3KC")
                .roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}
