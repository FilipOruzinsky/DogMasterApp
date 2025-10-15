package com.example.dogmasterapp.controller;

import com.example.dogmasterapp.entity.Training;
import com.example.dogmasterapp.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;

    @GetMapping
    public ResponseEntity<List<Training>> getAllTrainings() {
        return ResponseEntity.ok(trainingService.getAllTrainings());
    }
    @GetMapping("/in-current-month")
    public ResponseEntity<List<Training>> getAllTrainingsInCurrentMonth(@RequestParam(name = "month") LocalDateTime month) {
        return ResponseEntity.ok(trainingService.getAllTrainingsInCurrentMonth(month));
    }



    @PostMapping
    public ResponseEntity<Training> createTraining(@RequestBody Training training) {
        return ResponseEntity.ok(trainingService.createTraining(training));
    }
    @PostMapping("/{trainingID}/add-participant")
    public ResponseEntity<Training> addParticipantToTraining(@PathVariable Integer trainingID) {
        return ResponseEntity.ok(trainingService.addParticipant(trainingID));
    }








}
