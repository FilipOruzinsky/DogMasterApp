package com.example.dogmasterapp.dto;


import com.example.dogmasterapp.entity.TrainingType;

import java.time.LocalDate;

public record TrainingDTO(
        TrainingType trainingtype,
        String trainingsNames,
        LocalDate trainingDate
) {

}
