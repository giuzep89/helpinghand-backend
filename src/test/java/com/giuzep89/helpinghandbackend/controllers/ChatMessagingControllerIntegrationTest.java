package com.giuzep89.helpinghandbackend.controllers;

import com.giuzep89.helpinghandbackend.models.Authority;
import com.giuzep89.helpinghandbackend.models.User;
import com.giuzep89.helpinghandbackend.repositories.AuthorityRepository;
import com.giuzep89.helpinghandbackend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class ChatMessagingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User otherUser;

    @BeforeEach
    void setUp() {
        Authority roleUser = authorityRepository.save(new Authority("ROLE_USER"));

        User initiator = new User();
        initiator.setUsername("testuser");
        initiator.setEmail("testuser@example.com");
        initiator.setPassword(passwordEncoder.encode("password123"));
        initiator.setAuthorities(Set.of(roleUser));
        userRepository.save(initiator);

        otherUser = new User();
        otherUser.setUsername("otheruser");
        otherUser.setEmail("otheruser@example.com");
        otherUser.setPassword(passwordEncoder.encode("password123"));
        otherUser.setAuthorities(Set.of(roleUser));
        otherUser = userRepository.save(otherUser);
    }

    @Test
    @WithMockUser(username = "testuser")
    void createChat_returnsCreatedWithCorrectBody() throws Exception {
        mockMvc.perform(post("/chats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(otherUser.getId())))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.otherUserUsername").value("otheruser"));
    }
}
