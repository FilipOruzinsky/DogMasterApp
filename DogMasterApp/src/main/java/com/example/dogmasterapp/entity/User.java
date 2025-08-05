package com.example.dogmasterapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name= "users")

public class User {
    @Id
    @GeneratedValue
    public Integer userID;
    @NotBlank(message= "Your name is required")
    public String firstName;
    @NotBlank(message= "Your sure name is required")
    public String lastName;
    public String address;
    @NotBlank(message= " Phone number is required")
    public String phoneNumber;
    @NotBlank(message= " Email is required")
    public String email;
    @NotBlank(message= "Password is required")
    public String password;
}
