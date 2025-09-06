package com.example.dogmasterapp.service;

import com.example.dogmasterapp.entity.Dog;
import com.example.dogmasterapp.entity.User;
import com.example.dogmasterapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    Authentication authentication;

    @Mock
    Jwt jwt;

    @Mock
    UserRepository userRepository;

    @Mock
    SecurityContext securityContext;

    @InjectMocks
    UserService userService;


    @Test
    @DisplayName("getCurrentUser: vráti existujúceho používateľa z DB")
    void getCurrentUser_existingUser() {
        String userId = "1";
        User existingUser = new User();
        existingUser.setUserID(userId);

        when(jwt.getSubject()).thenReturn(userId);

        when(authentication.getPrincipal()).thenReturn(jwt);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        User result = userService.getCurrentUser();

        assertThat(result).isSameAs(existingUser);

        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }
    @Test
    @DisplayName("createUserFromJwt: vytvorenie usera z tokenu")
    void createUserFromJasonWebToken() {

        when(jwt.getSubject()).thenReturn("1");
        when(jwt.getClaimAsString("preferred_username")).thenReturn("jozino");
        when(jwt.getClaimAsString("email")).thenReturn("jozino@gmail.com");
        when(jwt.getClaimAsString("given_name")).thenReturn("Jozef");
        when(jwt.getClaimAsString("family_name")).thenReturn("Mrkva");
        when(jwt.getClaimAsString("address")).thenReturn("Zamocka 5");
        when(jwt.getClaimAsString("phone_number")).thenReturn("0918753963");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));


        User user = userService.createUserFromJwt(jwt);

        assertEquals("1", user.getUserID());
        assertEquals("jozino", user.getUserName());
        assertEquals("jozino@gmail.com", user.getEmail());
        assertEquals("Jozef", user.getFirstName());
        assertEquals("Mrkva", user.getLastName());
        assertEquals("Zamocka 5", user.getAddress());
        assertEquals("0918753963", user.getPhoneNumber());

    }

}