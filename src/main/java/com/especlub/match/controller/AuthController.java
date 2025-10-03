package com.especlub.match.controller;

import com.especlub.match.dto.request.LoginInternalRequest;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.services.interfaces.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    // injects dependencies
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JsonDtoResponse<Boolean>> login(@RequestBody LoginInternalRequest loginInternalRequest,
                                                          HttpServletRequest request,
                                                          HttpServletResponse responseHttp) {
        boolean result = authService.loginSession(loginInternalRequest, request, responseHttp);
        return JsonDtoResponse.ok("Inicio de sesión correcto", result).toResponseEntity();
    }

    @GetMapping("/validate")
    public ResponseEntity<JsonDtoResponse<Void>> validateToken(HttpServletRequest request) {
        authService.validateUserJWT(request);
        return JsonDtoResponse.<Void>ok("Token validado",null).toResponseEntity();
    }

    @PostMapping("/logout")
    public ResponseEntity<JsonDtoResponse<Boolean>> logout(HttpServletRequest request, HttpServletResponse responseHttp) {
        boolean result = authService.logoutSession(request, responseHttp);
        return JsonDtoResponse.ok("Cierre de sesión correcto", result).toResponseEntity();
    }

}
