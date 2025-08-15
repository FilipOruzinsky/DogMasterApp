package com.example.dogmasterapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name= "dogs")
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer dogID;
    @ManyToOne
    @JoinColumn(name = "ownerID")
    @JsonIgnore
    public User owner;
    public String name;
    public String breed;
    public int age;
}
