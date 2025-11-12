package com.example.dogmasterapp.controller;

import com.example.dogmasterapp.entity.Answer;
import com.example.dogmasterapp.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<Answer> createAnswer(@RequestParam(name = "question-id") Integer questionId, @RequestBody Answer answer) {
        return ResponseEntity.ok(answerService.createAnswer(questionId, answer));
    }

    @PostMapping("/{id}/up")
    public ResponseEntity<Answer> voteForAnswerUp(@PathVariable Integer id) {
        return ResponseEntity.ok(answerService.voteForAnswer(id, 1));
    }

    @PostMapping("/{id}/down")
    public ResponseEntity<Answer> voteForAnswerDown(@PathVariable Integer id) {
        return ResponseEntity.ok(answerService.voteForAnswer(id, -1));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Answer> markAsBest(@PathVariable Integer id) {
        return ResponseEntity.ok(answerService.markAsBest(id));
    }
}
