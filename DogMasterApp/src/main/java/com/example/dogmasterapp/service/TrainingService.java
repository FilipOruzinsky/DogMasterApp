package com.example.dogmasterapp.service;

import com.example.dogmasterapp.entity.Training;
import com.example.dogmasterapp.entity.User;
import com.example.dogmasterapp.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

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

    public List<Training> getAllTrainingInThisMonthFromToday(LocalDateTime today) {
       List<Training> allTrainings = trainingRepository.findAll();
        Stream<Training> trainingStream = allTrainings.stream().filter((training) -> today.getYear() == training.getTrainingDateTime().getYear() && today.getMonth() == training.getTrainingDateTime().getMonth() && training.getTrainingDateTime().getDayOfMonth() >= today.getDayOfMonth() );
        return trainingStream.toList();
    }


}
