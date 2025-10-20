package com.example.dogmasterapp.service;

import com.example.dogmasterapp.entity.Answer;
import com.example.dogmasterapp.entity.Question;
import com.example.dogmasterapp.entity.User;
import com.example.dogmasterapp.repository.AnswerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnswerServiceTest {

    @Mock
    AnswerRepository answerRepository;

    @Mock
    QuestionService questionService;

    @Mock
    UserService userService;

    @InjectMocks
    AnswerService answerService;

    private Question mockQuestion(String userId) {
        Question q = new Question();
        q.setQuestion("Why is my dog always barking?");
        q.setQuestioner(mockUser(userId));
        q.setClosed(false);
        return q;
    }

    private Answer mockAnswer(Question question, User user) {
        Answer a = new Answer();
        a.setAnswer("This is a test answer");
        a.setVotes(0);
        a.setQuestion(question);
        a.setAnswerer(user);
        a.setIsBestOne(false);
        return a;
    }

    private User mockUser(String id) {
        User u = new User();
        u.setUserID(id);
        return u;
    }

    @Test
    @DisplayName("createAnswer")
    void createAnswer() {
        User currentUser = mockUser("u-123");
        Question question = mockQuestion(currentUser.getUserID());
        Answer answer = mockAnswer(question, currentUser);

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(questionService.getQuestionById(1)).thenReturn(question);
        when(answerRepository.save(any(Answer.class))).thenReturn(answer);

        Answer result = answerService.createAnswer(1, answer);

        ArgumentCaptor<Answer> toSave = ArgumentCaptor.forClass(Answer.class);
        verify(answerRepository).save(toSave.capture());
        assertThat(result).isSameAs(answer);
        assertThat(result.getQuestion()).isSameAs(question);
        assertThat(result.getAnswerer()).isSameAs(currentUser);
        verifyNoMoreInteractions(answerRepository, questionService, userService);
    }

    @Test
    @DisplayName("voteForAnswer")
    void voteForAnswer() {
        Answer a = mockAnswer(mockQuestion("u-123"), mockUser("u-123"));

        when(userService.getCurrentUser()).thenReturn(mockUser("u-124"));
        when(answerRepository.findById(1)).thenReturn(Optional.of(a));

        assertThat(a.getVotes()).isEqualTo(0);
        answerService.voteForAnswer(1, 1);
        assertThat(a.getVotes()).isEqualTo(1);
    }

    @Test
    @DisplayName("voteForAnswer: throws an error when voting for own answer")
    void voteForAnswer_ownAnswer() {
        Answer a = mockAnswer(mockQuestion("u-123"), mockUser("u-123"));

        when(userService.getCurrentUser()).thenReturn(mockUser("u-123"));
        when(answerRepository.findById(1)).thenReturn(Optional.of(a));

        assertThat(a.getVotes()).isEqualTo(0);
        assertThatThrownBy(() -> answerService.voteForAnswer(1, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot vote for your own answer");
    }

    @Test
    @DisplayName("markAsBest")
    void markAsBest() {
        Answer a = mockAnswer(mockQuestion("u-123"), mockUser("u-123"));

        when(userService.getCurrentUser()).thenReturn(mockUser("u-123"));
        when(answerRepository.findById(1)).thenReturn(Optional.of(a));

        assertThat(a.getIsBestOne()).isEqualTo(false);
        answerService.markAsBest(1);
        assertThat(a.getIsBestOne()).isEqualTo(true);
    }

    @Test
    @DisplayName("markAsBest: throws an error when marking best answer as not owner of the question")
    void markAsBest_notOwner() {
        Answer a = mockAnswer(mockQuestion("u-123"), mockUser("u-123"));

        when(userService.getCurrentUser()).thenReturn(mockUser("u-124"));
        when(answerRepository.findById(1)).thenReturn(Optional.of(a));

        assertThat(a.getIsBestOne()).isEqualTo(false);
        assertThatThrownBy(() -> answerService.markAsBest(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Only the owner can mark an answer as best");
    }
}
