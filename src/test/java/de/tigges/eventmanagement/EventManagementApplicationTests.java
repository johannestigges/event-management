package de.tigges.eventmanagement;

import de.tigges.eventmanagement.rest.users.Instrument;
import de.tigges.eventmanagement.rest.users.User;
import de.tigges.eventmanagement.rest.users.UserRepository;
import de.tigges.eventmanagement.rest.users.UserStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
                .andExpect(jsonPath("$[0].instrument.id", is(1)))
                .andExpect(jsonPath("$[0].instrument.instrument", is("Violine")))
                .andExpect(jsonPath("$[0].instrumentId").doesNotExist())
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

    @Test
    @WithMockUser
    void updateUser() throws Exception {
        var body = """
                {
                  "id": %d,
                  "version": %d,
                  "vorname": "Peter",
                  "nachname": "Dampf",
                  "status": "Gast",
                  "instrument": {"id": 2, "instrument": "Viola", "gruppe": "Streicher"}
                }
                """.formatted(user.id(), user.version());

        mvc.perform(put("/rest/users/" + user.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vorname", is("Peter")))
                .andExpect(jsonPath("$.version", is(user.version().intValue() + 1)))
                .andExpect(jsonPath("$.instrument.id", is(2)));

        var updated = userRepository.findById(user.id()).orElseThrow();
        assertThat(updated.instrumentId()).isEqualTo(2L);
        assertThat(updated.version()).isEqualTo(user.version() + 1);
    }

    private static User createUser() {
        return new User(
                null,
                null,
                "Hans",
                "Dampf",
                UserStatus.Gast,
                1L,
                createInstrument()
        );
    }

    private static Instrument createInstrument() {
        return new Instrument(1L, "Violine", "Streicher");
    }
}