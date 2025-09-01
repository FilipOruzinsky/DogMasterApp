package com.example.dogmasterapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name= "users")

public class User {
    @Id
    public String userID;
    public String userName;
    public String firstName;
    public String lastName;
    public String address;
    public String phoneNumber;
    public String email;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    public List<Dog> dogs;
}
