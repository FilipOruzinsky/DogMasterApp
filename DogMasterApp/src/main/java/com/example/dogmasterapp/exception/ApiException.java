package com.example.dogmasterapp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ApiException {
    private String message;
    List<String> roles = Arrays.asList("USER","ADMIN");
    private HttpStatus httpStatus;
    private String timestamp;
    private Object details;
    private String requiredPermission;
    private String endpoint;
}
