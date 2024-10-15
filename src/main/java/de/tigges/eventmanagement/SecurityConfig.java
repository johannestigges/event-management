package de.tigges.eventmanagement;

import de.tigges.eventmanagement.UsersData.UserData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final Duration TOKEN_VALIDITY = Duration.ofDays(365);

    private final UsersData usersData;

    @Value("${login.remember-me.key}")
    private String rememberMeKey;

    private static void requestConfig(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry requests) {
        requests
                .requestMatchers(new AntPathRequestMatcher("/rest/**")).authenticated()
                .anyRequest().permitAll();
    }

    private static void formLoginConfiguration(FormLoginConfigurer<HttpSecurity> formLogin) {
        formLogin
                .loginPage("/#/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .failureHandler(new AppAuthenticationFailureHandler());
    }

    private static UserDetails map(UserData user) {
        return User.builder()
                .username(user.username())
                .password(user.password())
                .roles(user.roles())
                .build();
    }

    private static int toSeconds(Duration duration) {
        return Long.valueOf(duration.getSeconds()).intValue();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.formLogin(SecurityConfig::formLoginConfiguration)
                .authorizeHttpRequests(SecurityConfig::requestConfig)
                .csrf(AbstractHttpConfigurer::disable)
                .rememberMe(this::rememberMeConfiguration)
                .build();
    }

    @Bean
    public UserDetailsService users() {
        return new InMemoryUserDetailsManager(
                Arrays.stream(usersData.getUsers())
                        .map(SecurityConfig::map)
                        .collect(Collectors.toSet()));
    }

    private void rememberMeConfiguration(RememberMeConfigurer<HttpSecurity> rememberMe) {
        rememberMe
                .key(rememberMeKey)
                .alwaysRemember(true)
                .tokenValiditySeconds(toSeconds(TOKEN_VALIDITY));
    }

    private static class AppAuthenticationFailureHandler implements AuthenticationFailureHandler {
        @Override
        public void onAuthenticationFailure(
                HttpServletRequest request,
                HttpServletResponse response,
                AuthenticationException exception) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
