package com.example.dogmasterapp.service;

import com.example.dogmasterapp.dto.TrainingDTO;
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


    public Training createTraining(TrainingDTO trainingDTO) {
        User trainingUser = userService.getCurrentUser();

//        boolean exist =
//                trainingRepository.existsTrainingForUserOnDate(
//                       trainingUser.getUserID(),
//                        trainingDTO.trainingDate()
//                );

        var trainingExistsOnDate = trainingRepository.existsTrainingByTrainingDate(trainingDTO.trainingDate());

        if (trainingExistsOnDate) {
            throw new IllegalStateException(
                    "Training for that date already exists! If you want to join, add yourself as participant."
            );
        }
        Training training = new Training();
        training.trainingNames = trainingDTO.trainingsNames();
        training.trainingDate = trainingDTO.trainingDate();
        training.trainingType = trainingDTO.trainingtype();
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
        Stream<Training> trainingStream = allTrainings.stream().filter((training) -> today.getYear() == training.getTrainingDate().getYear() && today.getMonth() == training.getTrainingDate().getMonth() && training.getTrainingDate().getDayOfMonth() >= today.getDayOfMonth() );
        return trainingStream.toList();
    }


}
