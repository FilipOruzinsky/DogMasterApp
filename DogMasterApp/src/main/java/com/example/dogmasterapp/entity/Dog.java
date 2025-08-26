package com.example.dogmasterapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "dogs")

public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer dogID;
    @ManyToOne
    @JoinColumn(name = "owner_user_id")
    public User owner;
    public String name;
    public int age;
    public String breed;
}
