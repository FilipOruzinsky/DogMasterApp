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
    @Column(name = "userid")
    public String userID;
    @Column(name = "user_name")
    public String userName;
    @Column(name = "first_name")
    public String firstName;
    @Column(name = "last_name")
    public String lastName;
    public String address;
    @Column(name = "phone_number")
    public String phoneNumber;
    public String email;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    public List<Dog> dogs;
}
