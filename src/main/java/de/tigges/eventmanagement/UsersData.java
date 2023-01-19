package de.tigges.eventmanagement;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "event-magagement")
@Configuration("UserData")
@Data
public class UsersData {
    private UserData[] users;

    @Data
    public static class UserData {
        private String username;
        private String password;
        private String[] roles;
    }
}
