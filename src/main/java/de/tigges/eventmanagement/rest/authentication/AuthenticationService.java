package de.tigges.eventmanagement.rest.authentication;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Builder;
import lombok.Data;

@RestController
@RequestMapping("/rest/authentication")
public class AuthenticationService {

    @GetMapping("/me")
    LoggedInUser getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken || authentication == null) {
            throw new SessionAuthenticationException("no authentication context available");
        }
        var roles = authentication.getAuthorities().stream()
            .map(a -> a.getAuthority())
            .collect(Collectors.toSet());
        return LoggedInUser.builder()
                .name(authentication.getName())
                .roles(roles)
                .build();
    }

    @Data
    @Builder
    static class LoggedInUser {
        private String name;
        private Collection<String> roles;
    }
}
