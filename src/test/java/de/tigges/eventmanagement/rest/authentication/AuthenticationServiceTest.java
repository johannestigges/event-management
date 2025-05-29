package de.tigges.eventmanagement.rest.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

    @Test
    void getLoggedInUser() {
        initAuthentication("username", "role1", "role2", "role3");
        var loggedinUser = new AuthenticationService().getLoggedInUser();
        assertEquals(3, loggedinUser.roles().size());
        assertTrue(loggedinUser.roles().contains("role1"));
        assertTrue(loggedinUser.roles().contains("role2"));
        assertTrue(loggedinUser.roles().contains("role2"));
        assertEquals("username", loggedinUser.name());
    }

    @Test
    void returnAnonymousWithoutAuthentication() {
        var loggedInUser = new AuthenticationService().getLoggedInUser();
        assertThat(loggedInUser.name()).isEmpty();
        assertThat(loggedInUser.roles()).isEmpty();
    }

    @Test
    void anonymousAuthenticationToken() {
        var ctx = mock(SecurityContext.class);
        SecurityContextHolder.setContext(ctx);
        when(ctx.getAuthentication()).thenReturn(mock(AnonymousAuthenticationToken.class));

        var loggedInUser = new AuthenticationService().getLoggedInUser();

        assertThat(loggedInUser.name()).isEmpty();
        assertThat(loggedInUser.roles()).isEmpty();
    }

    private void initAuthentication(String username, String... roles) {
        var ctx = mock(SecurityContext.class);
        SecurityContextHolder.setContext(ctx);
        var authentication = mock(Authentication.class);
        when(ctx.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(authentication.getAuthorities()).thenReturn((Collection) createRoles(roles));
    }

    private Collection<GrantedAuthority> createRoles(String... roles) {
        return Stream.of(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
