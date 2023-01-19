package de.tigges.eventmanagement;

import de.tigges.eventmanagement.UsersData.UserData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UsersData usersData;

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
        return new InMemoryUserDetailsManager(
            Arrays.stream(usersData.getUsers())
                .map(SecurityConfig::map)
                .collect(Collectors.toSet()));
    }

    private static UserDetails map(UserData user) {
        return User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRoles())
            .build();
    }
}
