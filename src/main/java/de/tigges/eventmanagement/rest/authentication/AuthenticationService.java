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
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/authentication")
public class AuthenticationService {

    @GetMapping("/me")
    LoggedInUser getLoggedInUser() {
        return createLoggedinUser(getAuthentication());
    }

    private static Authentication getAuthentication() {
        return assertAuthenticated(SecurityContextHolder.getContext().getAuthentication());
    }

    private static Authentication assertAuthenticated(Authentication authentication) {
        if (isNotAuthenticated(authentication)) {
            throw new SessionAuthenticationException("no authentication context available");
        }
        return authentication;
    }

    private static boolean isNotAuthenticated(Authentication authentication) {
        return authentication instanceof AnonymousAuthenticationToken || authentication == null;
    }

    private static LoggedInUser createLoggedinUser(Authentication authentication) {
        return new LoggedInUser(authentication.getName(), getRoles(authentication));
    }

    private static Set<String> getRoles(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toUnmodifiableSet());
    }

    public record LoggedInUser(String name, Collection<String> roles) {
    }
}
