package com.example.dogmasterapp.repository;

import com.example.dogmasterapp.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TrainingRepository extends JpaRepository<Training, Integer> {

//    List<Training> getAllTrainingsInCurrentMonth(@RequestParam(name = "month") Integer month);
//    boolean existsTrainingForUserOnDate (@Param ("userId") String userID, @Param ("trainingDate") LocalDate trainingDate);

    boolean existsTrainingByTrainingDate(LocalDate trainingDate);

}
