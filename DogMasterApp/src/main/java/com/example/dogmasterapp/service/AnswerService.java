package com.example.dogmasterapp.service;

import com.example.dogmasterapp.entity.Answer;
import com.example.dogmasterapp.exception.AnswerNotFoundException;
import com.example.dogmasterapp.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final UserService userService;
    private final QuestionService questionService;
    private final Logger logger = LoggerFactory.getLogger(AnswerService.class);

    public Answer createAnswer(Integer questionId, Answer answer) {
        Answer answerToSave = Answer.builder()
                .answerer(userService.getCurrentUser())
                .answer(answer.getAnswer())
                .votes(0)
                .isBestOne(false)
                .question(questionService.getQuestionById(questionId))
                .build();
        Answer a = answerRepository.save(answerToSave);
        logger.info("Answer with ID: '{}' has been created for question ID: '{}'", a.id, questionId);
        return a;
    }

    public Answer voteForAnswer(Integer id, int vote) {
        Answer existingAnswer = answerRepository.findById(id).orElseThrow(() -> logAndThrow(id));
        if (existingAnswer.answerer.equals(userService.getCurrentUser()))
            throw new IllegalArgumentException("Cannot vote for your own answer");
        existingAnswer.votes += vote;
        return answerRepository.save(existingAnswer);
    }

    private AnswerNotFoundException logAndThrow(Integer id) {
        logger.error("Answer not found with id: {}", id);
        return new AnswerNotFoundException("Answer not found with id: " + id);
    }

    public Answer markAsBest(Integer id) {
        Answer existingAnswer = answerRepository.findById(id).orElseThrow(() -> logAndThrow(id));
        // only the owner can mark an answer as best
        if (existingAnswer.getQuestion() == null ||
                existingAnswer.getQuestion().getQuestioner() == null ||
                !existingAnswer.getQuestion().getQuestioner().equals(userService.getCurrentUser())) {
            throw new IllegalArgumentException("Only the owner can mark an answer as best");
        }
        existingAnswer.isBestOne = true;
        return answerRepository.save(existingAnswer);
    }
}
