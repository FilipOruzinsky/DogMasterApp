package com.example.dogmasterapp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer trainingID;
    public String trainingName; // obrana , poslusnost...
    @Enumerated(EnumType.STRING)
    public TrainingType trainingType;
    public LocalDate trainingDate;
    public LocalTime startTime;
    public LocalTime endTime;
    public Integer trainingParticipants;

    @ManyToMany
    @JoinTable(
            name = "training_participants",
            joinColumns = @JoinColumn(name = "trainingID"),
            inverseJoinColumns = @JoinColumn(name = "userID")
    )
    public List<User> participants;

}
