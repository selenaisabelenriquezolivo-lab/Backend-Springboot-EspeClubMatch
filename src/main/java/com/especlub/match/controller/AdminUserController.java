package com.especlub.match.controller;

import com.especlub.match.dto.response.AdminUserSummaryDto;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.models.UserInfo;
import com.especlub.match.services.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class AdminUserController {

    private final AuthServiceImpl authService;

    @GetMapping("/me")
    public ResponseEntity<JsonDtoResponse<AdminUserSummaryDto>> getCurrentUser(HttpServletRequest request) {
        UserInfo currentUser = authService.validateUserJWT(request);
        AdminUserSummaryDto summary = AdminUserSummaryDto.builder()
                .username(currentUser.getUsername())
                .email(currentUser.getEmail())
                .phone(currentUser.getPhone())
                .firstName(currentUser.getNames())
                .lastName(currentUser.getSurnames())
                .build();
        return JsonDtoResponse.ok("Usuario actual obtenido", summary).toResponseEntity();
    }

}
