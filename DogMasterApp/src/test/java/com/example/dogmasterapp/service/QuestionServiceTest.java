package com.example.dogmasterapp.service;

import com.example.dogmasterapp.entity.Question;
import com.example.dogmasterapp.entity.User;
import com.example.dogmasterapp.exception.QuestionNotFoundException;
import com.example.dogmasterapp.repository.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    QuestionRepository questionRepository;

    @Mock
    UserService userService;

    @InjectMocks
    QuestionService questionService;

    private User mockUser() {
        User u = new User();
        u.setUserID("u-123");
        return u;
    }

    private Question mockQuestion(User currentUser) {
        Question q = new Question();
        q.setQuestion("Why is my dog always barking?");
        q.setQuestioner(currentUser);
        q.setClosed(false);
        LocalDateTime now = LocalDateTime.now();
        q.setCreatedAt(now);
        q.setUpdatedAt(now);
        return q;
    }

    @Test
    @DisplayName("createQuestion")
    void createQuestion() {
        User currentUser = mockUser();
        Question question = mockQuestion(currentUser);

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        Question result = questionService.createQuestion(question);

        ArgumentCaptor<Question> toSave = ArgumentCaptor.forClass(Question.class);
        verify(questionRepository).save(toSave.capture());
        assertThat(result.getQuestioner()).isSameAs(currentUser);
        assertThat(result).isSameAs(question);
        verifyNoMoreInteractions(questionRepository, userService);
    }

    @Test
    @DisplayName("getQuestionById: not found")
    void getQuestionById_notFound() {
        when(questionRepository.findById(42)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> questionService.getQuestionById(42))
                .isInstanceOf(QuestionNotFoundException.class)
                .hasMessageContaining("42");
        verify(questionRepository).findById(42);
        verifyNoMoreInteractions(questionRepository);
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("getQuestionById: found")
    void getQuestionById_found() {
        Question question = mockQuestion(mockUser());
        when(questionRepository.findById(42)).thenReturn(Optional.of(question));

        Question result = questionService.getQuestionById(42);

        assertThat(result).isSameAs(question);
        verify(questionRepository).findById(42);
        verifyNoMoreInteractions(questionRepository);
        verifyNoInteractions(userService);
    }
}
