package com.example.dogmasterapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AnswerExceptionHandler {
    @ExceptionHandler(AnswerNotFoundException.class)
    public ResponseEntity<String> handleAnswerNotFoundException(AnswerNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(VotingForOwnAnswerException.class)
    public ResponseEntity<String> handleVotingForOwnAnswerException(VotingForOwnAnswerException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(NotOwnerException.class)
    public ResponseEntity<String> handleNotOwnerException(NotOwnerException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
