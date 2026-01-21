package com.example.dogmasterapp.service;

import com.example.dogmasterapp.dto.UserDTO;
import com.example.dogmasterapp.entity.User;
import com.example.dogmasterapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public User createUserFromJwt(Jwt jwt) {
        User user = new User();

        user.setUserID(jwt.getSubject());
        user.setUserName(jwt.getClaimAsString("preferred_username"));
        user.setEmail(jwt.getClaimAsString("email"));
        user.setFirstName(jwt.getClaimAsString("given_name"));
        user.setLastName(jwt.getClaimAsString("family_name"));
        user.setAddress(jwt.getClaimAsString("address"));
        user.setPhoneNumber(jwt.getClaimAsString("phoneNumber"));

        return userRepository.save(user);
    }

    public User getCurrentUser() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findById(jwt.getSubject())
                .orElseGet(() -> createUserFromJwt(jwt));
    }

    /**
     * Updates and persists the current user with DTO
     */
    @Transactional
    public User updateCurrentUser(UserDTO userDTO) { //tu je na vstupe UserDto lebo FE posle UserDto ale do DB to chceme ulozit ako Usera tak premapujeme UserDto na User ??
        User currentUser = getCurrentUser();
        currentUser.firstName = userDTO.firstName();
        currentUser.lastName = userDTO.lastName();
        currentUser.address = userDTO.address();
        currentUser.phoneNumber = userDTO.phoneNumber();
        currentUser.email = userDTO.email();

        updateKeycloakUser(currentUser);

        return userRepository.save(currentUser);
    }

    /**
     * Gets admin access token from Keycloak
     */
    private String getAdminAccessToken() {
        String url = "http://localhost:9099/realms/master/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", "admin-cli");
        map.add("username", "temp-admin"); // From your docker-compose
        map.add("password", "temp-admin");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        var response = restTemplate.postForObject(url, request, Map.class);

        assert response != null;
        return (String) response.get("access_token");
    }

    /**
     * Updates Keycloak user with provided details
     */
    private void updateKeycloakUser(User user) {
        String adminToken = getAdminAccessToken();
        String realm = "dog-master-realm";
        String url = "http://localhost:9099/admin/realms/" + realm + "/users/" + user.getUserID();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Map your User entity fields to Keycloak's UserRepresentation
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", user.getFirstName());
        updates.put("lastName", user.getLastName());
        updates.put("email", user.getEmail());
        // Maps user details to a Keycloak attribute list
        updates.put("attributes", Map.of(
                "address", List.of(user.getAddress() != null ? user.getAddress() : ""),
                "phoneNumber", List.of(user.getPhoneNumber() != null ? user.getPhoneNumber() : "")
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updates, headers);
        try {
            restTemplate.put(url, entity);
            System.out.println("Keycloak user updated successfully");
        } catch (Exception e) {
            System.err.println("Failed to update Keycloak: " + e.getMessage());
        }
    }
}
