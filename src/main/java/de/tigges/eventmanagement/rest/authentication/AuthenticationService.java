package de.tigges.eventmanagement.rest.authentication;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/authentication")
public class AuthenticationService {

    @GetMapping("/me")
    LoggedInUser getLoggedInUser() {
        return getAuthenticationFromContext()
                .map(AuthenticationService::toLoggedInUser)
                .orElseThrow(AuthenticationService::authenticationException);
    }

    private static Optional<Authentication> getAuthenticationFromContext() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .flatMap(AuthenticationService::assertAuthenticated);
    }

    private static Optional<Authentication> assertAuthenticated(Authentication authentication) {
        return isAuthenticated(authentication) ? Optional.of(authentication) : Optional.empty();
    }

    private static boolean isAuthenticated(Authentication authentication) {
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }

    private static LoggedInUser toLoggedInUser(Authentication authentication) {
        return new LoggedInUser(authentication.getName(), getRoles(authentication));
    }

    private static SessionAuthenticationException authenticationException() {
        return new SessionAuthenticationException("no authentication context available");
    }

    private static Set<String> getRoles(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toUnmodifiableSet());
    }

    public record LoggedInUser(String name, Collection<String> roles) {
    }
}
