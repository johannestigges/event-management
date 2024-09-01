package de.tigges.eventmanagement;

import de.tigges.eventmanagement.rest.users.Instrument;
import de.tigges.eventmanagement.rest.users.User;
import de.tigges.eventmanagement.rest.users.UserRepository;
import de.tigges.eventmanagement.rest.users.UserStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EvRentManagementApplicationTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void addUser() {
        user = userRepository.insert(createUser());
    }

    @AfterEach
    void deleteUser() {
        userRepository.deleteById(user.id());
    }

    @Test
    @WithMockUser
    void getInstruments() throws Exception {
        mvc.perform(get("/rest/users/instruments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(18)))
                .andExpect(jsonPath("$[0].id", is(0)))
                .andExpect(jsonPath("$[0].instrument", is("Dirigent")))
        ;
    }

    @Test
    @WithMockUser(username = "ADMIN")
    void getUsers() throws Exception {

        mvc.perform(get("/rest/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(user.id().intValue())))
                .andExpect(jsonPath("$[0].version", is(user.version().intValue())))
                .andReturn()
        ;
    }

    @Test
    @WithMockUser
    void getUser() throws Exception {

        mvc.perform(get("/rest/users/" + user.id()))
                .andExpect(status().isOk()
                );
    }

    private static User createUser() {
        return new User(
                100L,
                1L,
                "Hans",
                "Dampf",
                UserStatus.Gast,
                createInstrument()
        );
    }

    private static Instrument createInstrument() {
        return new Instrument(1L, "Violine", "Streicher");
    }
}