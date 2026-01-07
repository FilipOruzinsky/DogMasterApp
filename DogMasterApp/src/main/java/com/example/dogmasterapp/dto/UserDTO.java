package com.example.dogmasterapp.dto;

import lombok.Data;


public record UserDTO(

        String firstName,
        String lastName,
        String address,
        String phoneNumber,
        String email

){}
