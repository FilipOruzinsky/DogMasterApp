package com.example.dogmasterapp.controller;

import com.example.dogmasterapp.entity.Answer;
import com.example.dogmasterapp.entity.Question;
import com.example.dogmasterapp.repository.AnswerRepository;
import com.example.dogmasterapp.repository.QuestionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnswerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @Order(1)
    @DisplayName("createAnswer")
    public void createAnswer() throws Exception {
        Question question = new Question();
        question.setQuestion("Why is my dog always barking?");

        mockMvc.perform(post("/api/v1/questions")
                        .with(jwtWithSub("questioner"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isOk());

        Integer questionId = questionRepository.findAll().get(0).getId();

        Answer answer = new Answer();
        answer.setAnswer("Please call us at 'xyz' for consulting: thanks :)");

        mockMvc.perform(post("/api/v1/answers?question-id=" + questionId)
                        .with(jwtWithSub("answerer"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answer)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @DisplayName("voteForAnswer: increase the vote count by 1 then decrease by 1")
    public void voteForAnswerUpThenDown() throws Exception {
        // create a question
        Question question = new Question();
        question.setQuestion("Why is my dog always barking?");
        mockMvc.perform(post("/api/v1/questions")
                        .with(jwtWithSub("questioner1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isOk());
        Integer questionId = questionRepository.findAll().get(1).getId();

        // create an answer
        Answer answer = new Answer();
        answer.setAnswer("Please call us at 'xyz' for consulting: thanks :)");

        String created = mockMvc.perform(post("/api/v1/answers?question-id=" + questionId)
                        .with(jwtWithSub("answerer1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answer)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Answer saved = objectMapper.readValue(created, Answer.class);

        // vote as a different user up
        mockMvc.perform(post("/api/v1/answers/" + saved.getId() + "/up")
                        .with(jwtWithSub("somebody-else"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.votes").value(1));

        // vote as a different user down
        mockMvc.perform(post("/api/v1/answers/" + saved.getId() + "/down")
                        .with(jwtWithSub("somebody-else-2"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.votes").value(0));
    }

    @Test
    @Order(3)
    @DisplayName("markAsBest")
    public void markAsBest() throws Exception {
        Question question = new Question();
        question.setQuestion("Why is my dog always barking?");

        mockMvc.perform(post("/api/v1/questions")
                        .with(jwtWithSub("questioner5"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isOk());

        Integer questionId = questionRepository.findAll().get(2).getId();

        Answer answer = new Answer();
        answer.setAnswer("Because it might be aggressive");

        mockMvc.perform(post("/api/v1/answers?question-id=" + questionId)
                        .with(jwtWithSub("answerer"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answer)))
                .andExpect(status().isOk());

        Integer answerId = answerRepository.findAll().get(2).getId();

        mockMvc.perform(post("/api/v1/answers/{id}", answerId)
                        .with(jwtWithSub("questioner5")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isBestOne").value(true));
    }

    private RequestPostProcessor jwtWithSub(String sub) {
        return jwt()
                .jwt(j -> j.claim("sub", sub))
                .authorities(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
