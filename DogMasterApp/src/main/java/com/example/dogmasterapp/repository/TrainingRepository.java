package com.example.dogmasterapp.repository;

import com.example.dogmasterapp.entity.Training;
import com.example.dogmasterapp.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Integer> {
    //zobrazi vsetky aj individual aj group v danom mesiaci
    List<Training> findByTrainingDateBetween(LocalDate start, LocalDate end);

    /*
     User klikne na kalendár a vyberie konkrétny deň (napr. 15. október 2025)
Čo musi zobrazit:  všetky dostupné tréningy v ten deň - aj individual aj group
Príklad: "Ukáž mi všetky tréningy na 15.10.2025" --musi byt zobrazena casova os
     */
    List<Training> findByTrainingDate(LocalDate trainingDate);


    /*
    User má preferenciu a chce vidieť len jeden typ tréningu
    Príklad:  "Všetky Individual tréningy v septembri"
               "Všetky Group tréningy tento týždeň"
               "Individuálne tréningy od 1.10 do 15.10"
    */
    List<Training> findByTrainingTypeAndTrainingDateBetween(TrainingType trainingType, LocalDate start, LocalDate end);

    // Pre zobrazenie konkrétneho typu v konkrétny deň
    List<Training> findByTrainingDateAndTrainingType(LocalDate trainingDate, TrainingType trainingType);

}
