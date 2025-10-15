package com.example.dogmasterapp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public LocalDateTime trainingDateTime;
    public Integer countOfTrainingParticipants =0;

    @ManyToMany(cascade = CascadeType.ALL)

//    @JoinTable(
//            name = "training_participants",
//            joinColumns = @JoinColumn(name = "trainingID"),
//            inverseJoinColumns = @JoinColumn(name = "userID")
//    )
    public List<User> participants = new ArrayList<>();

}
