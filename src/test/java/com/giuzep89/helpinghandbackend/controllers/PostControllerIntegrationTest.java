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
class PostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        Authority roleUser = authorityRepository.save(new Authority("ROLE_USER"));

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setAuthorities(Set.of(roleUser));
        userRepository.save(user);
    }

    @Test
    @WithMockUser(username = "testuser")
    void createHelpRequest_returnsCreatedWithCorrectBody() throws Exception {
        String json = """
                {
                    "description": "Need help with garden",
                    "helpType": "GARDENING"
                }
                """;

        mockMvc.perform(post("/posts/help-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.postType").value("HELP_REQUEST"))
                .andExpect(jsonPath("$.helpType").value("GARDENING"))
                .andExpect(jsonPath("$.authorUsername").value("testuser"))
                .andExpect(jsonPath("$.helpFound").value(false));
    }
}
