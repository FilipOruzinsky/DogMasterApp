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
@Table(name = "trainingsNames")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer trainingID;

    @Column(name = "training_name")
    public String trainingName; // obrana , poslusnost...
    @Column(name = "training_type")
    @Enumerated(EnumType.STRING)
    public TrainingType trainingType;
    @Column(name = "training_date_time")
    public LocalDateTime trainingDateTime;
    @Column(name = "count_of_training_participants")
    public Integer countOfTrainingParticipants =0;

    @ManyToMany(cascade = CascadeType.ALL) // todo: maybe check recommended way?
    @JoinTable(
            name = "training_participants",
            joinColumns = @JoinColumn(name = "trainingid"),
            inverseJoinColumns = @JoinColumn(name = "userid")
    )
    public List<User> participants = new ArrayList<>();

}
