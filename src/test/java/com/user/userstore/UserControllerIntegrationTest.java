package com.user.userstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.userstore.model.Address;
import com.user.userstore.model.User;
import com.user.userstore.service.UserService;
import com.user.userstore.utility.CsvUserParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CsvUserParser csvUserParser;

    private User testUser;

    @BeforeEach
    void setUp() {
        Address address = new Address("Main St", "Mumbai", "India");
        testUser = new User("aditya", "aditya@example.com", address);
    }

    @Test
    void createUser_withJson_shouldReturn201() throws Exception {
        when(userService.saveUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("aditya"))
                .andExpect(jsonPath("$.email").value("aditya@example.com"))
                .andExpect(jsonPath("$.address.city").value("Mumbai"));
    }

    @Test
    void createUser_withJson_shouldReturn400WhenBodyEmpty() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUser_shouldReturn200WhenFound() throws Exception {
        when(userService.getUserByUsername("aditya")).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/api/users/aditya"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("aditya"))
                .andExpect(jsonPath("$.address.country").value("India"));
    }

    @Test
    void getUser_shouldReturn404WhenNotFound() throws Exception {
        when(userService.getUserByUsername("unknown")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/unknown"))
                .andExpect(status().isNotFound());
    }

}
