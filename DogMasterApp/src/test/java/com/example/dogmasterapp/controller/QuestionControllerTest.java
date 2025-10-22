package com.example.dogmasterapp.controller;

import com.example.dogmasterapp.entity.Question;
import com.example.dogmasterapp.repository.QuestionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("createQuestion")
    public void createQuestion() throws Exception {
        Question q = new Question();
        q.setQuestion("Why is my dog always barking?");


        mockMvc.perform(post("/api/v1/questions")
                        .with(jwtWithSub())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(q)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("getQuestionById")
    public void getQuestionById() throws Exception {
        Question q = new Question();
        q.setQuestion("Why is my dog always barking?");
        q = questionRepository.save(q);

        mockMvc.perform(get("/api/v1/questions/{id}", q.getId())
                        .with(jwtWithSub())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value(q.getQuestion()));
    }

    private RequestPostProcessor jwtWithSub() {
        return jwt()
                .jwt(j -> j.claim("sub", "test-user"))
                .authorities(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
