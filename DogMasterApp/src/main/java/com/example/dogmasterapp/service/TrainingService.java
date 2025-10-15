package com.example.dogmasterapp.service;

import com.example.dogmasterapp.entity.Training;
import com.example.dogmasterapp.entity.User;
import com.example.dogmasterapp.repository.TrainingRepository;
import com.example.dogmasterapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final UserService userService;

    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }


    public Training createTraining(Training training) {
        User trainingUser = userService.getCurrentUser();
        training.participants.add(trainingUser);
        training.countOfTrainingParticipants++;
        return trainingRepository.save(training);
    }

    public Training addParticipant(Integer trainingID) {
        Training training =  trainingRepository.findById(trainingID).orElseThrow(IllegalArgumentException::new);
        User trainingUser = userService.getCurrentUser();
        if(training.participants.contains(trainingUser)) {
            throw  new IllegalArgumentException("User has scheduled this training already");
        }
        training.participants.add(trainingUser);
        training.countOfTrainingParticipants++;
        return trainingRepository.save(training);


    }

    public List<Training> getAlltrainingInThisMonthFromToday(LocalDateTime today) {
 //TODO add logic
        return null;
    }
}
