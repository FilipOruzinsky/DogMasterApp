package com.example.dogmasterapp.security;

import com.example.dogmasterapp.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS xxx");

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // Získanie používateľských informácií
        String username = request.getRemoteUser() != null ? request.getRemoteUser() : "Anonymous";
        String method = request.getMethod();
        String requestUri = request.getRequestURI();
        String endpoint = method + " " + requestUri;

        // Získanie rolí z SecurityContext
        List<String> userRoles = new ArrayList<>();
        String requiredPermission = determineRequiredPermission(method, requestUri);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            userRoles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        }

        log.warn("Access denied: User '{}' with roles {} attempted {} (required: {})",
                username, userRoles, endpoint, requiredPermission);

        // Nastavenie response
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // Vytvorenie rozšírenej ApiException pomocou @AllArgsConstructor
        ApiException apiException = new ApiException(
                "User is forbidden",                      // message
                userRoles,                                // roles
                HttpStatus.FORBIDDEN,                     // httpStatus
                ZonedDateTime.now().format(FORMATTER),    // timestamp
                null,                                     // details
                requiredPermission,                       // requiredPermission
                endpoint                                  // endpoint
        );

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(apiException));
    }

    private String determineRequiredPermission(String method, String requestUri) {
        // Mapovanie endpointov na potrebné oprávnenia
        if (method.equals("DELETE") && requestUri.startsWith("/api/v1/users/")) {
            return "ROLE_ADMIN";
        }
        if (requestUri.startsWith("/api/v1/dogs/")) {
            return "ROLE_USER";
        }
        return "AUTHENTICATED";
    }
}