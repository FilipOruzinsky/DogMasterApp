package com.example.dogmasterapp.controller;


import com.example.dogmasterapp.entity.User;
import com.example.dogmasterapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void returnAuthenticatedUser() throws Exception {
        User user1 = new User();
        user1.setUserID("123");
        user1.setUserName("JohnDoe");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setAddress("Zahradna 15");
        user1.setPhoneNumber("1234567890");
        user1.setEmail("johnDoe@gmail.com");

        userRepository.save(user1);

        //autentifikacia usera

        Jwt jwt = Jwt.withTokenValue("fake-token")
                .header("alg", "none")
                .subject(user1.getUserID())
                .claim("scope", "USER")
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new JwtAuthenticationToken(jwt, Collections.emptyList())
        );

        mockMvc.perform(get("/api/v1/users/me")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("JohnDoe"))
                .andExpect(jsonPath("$.email").value("johnDoe@gmail.com"));


    }

}
