package com.example.dogmasterapp.service;

import com.example.dogmasterapp.entity.User;
import com.example.dogmasterapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private User createUserFromJwt(Jwt jwt) {
        User user = new User();

        user.setUserID(jwt.getSubject());
        user.setUserName(jwt.getClaimAsString("preferred_username"));
        user.setEmail(jwt.getClaimAsString("email"));
        user.setFirstName(jwt.getClaimAsString("given_name"));
        user.setLastName(jwt.getClaimAsString("family_name"));
        user.setAddress(jwt.getClaimAsString("address"));
        user.setPhoneNumber(jwt.getClaimAsString("phone_number"));

        return userRepository.save(user);
    }

    public User getCurrentUser() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findById(jwt.getSubject())
                .orElseGet(() -> createUserFromJwt(jwt));
    }
}
