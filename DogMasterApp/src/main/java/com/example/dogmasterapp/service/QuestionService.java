package com.example.dogmasterapp.service;

import com.example.dogmasterapp.entity.Question;
import com.example.dogmasterapp.exception.QuestionNotFoundException;
import com.example.dogmasterapp.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    public Question createQuestion(Question question) {
        Question questionToSave = Question.builder()
                .questioner(userService.getCurrentUser())
                .question(question.getQuestion())
                .closed(false)
                .build();
        Question q = questionRepository.save(questionToSave);
        logger.info("Question with ID: '{}' has been created", q.id);
        return q;
    }

    public Question getQuestionById(Integer id) {
        return questionRepository.findById(id).orElseThrow(() -> logAndThrow(id));
    }

    private QuestionNotFoundException logAndThrow(Integer id) {
        logger.error("Question not found with id: {}", id);
        return new QuestionNotFoundException("Question not found with id: " + id);
    }
}
