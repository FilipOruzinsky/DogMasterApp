package com.example.dogmasterapp.repository;

import com.example.dogmasterapp.entity.Training;
import com.example.dogmasterapp.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Integer> {

    List<Training> getAllTrainingsInCurrentMonth(@RequestParam(name = "month") Integer month);


}
