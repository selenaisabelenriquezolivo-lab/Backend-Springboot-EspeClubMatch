package com.especlub.match.services.impl;

import com.especlub.match.dto.request.LoginInternalRequest;
import com.especlub.match.exceptions.CustomExceptions;
import com.especlub.match.models.UserInfo;
import com.especlub.match.repositories.UserInfoRepository;
import com.especlub.match.security.jwt.JwtProvider;
import com.especlub.match.services.interfaces.AuthService;
import com.especlub.match.utils.CookieUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // injected dependencies
    private final UserInfoRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder encoder;
    private final CookieUtils cookieUtils;

    // declarate variables
    String message;

    public boolean loginSession(LoginInternalRequest loginRequest, HttpServletRequest request, HttpServletResponse responseHttp) {
        log.info("Login user: {}", loginRequest.getUsername());

        UserInfo user = userRepository.findByUsernameAndRecordStatusTrue(loginRequest.getUsername());
        if (user == null || !encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomExceptions("Usuario o contraseña incorrectos", HttpStatus.UNAUTHORIZED.value());
        }

        String jwt = jwtProvider.generateJwtByUsername(user);
        cookieUtils.addTokenCookie(request, responseHttp, jwt);
        return true;
    }


    @Override
    public UserInfo validateUserJWT(HttpServletRequest request) {
        String token = cookieUtils.extractTokenFromHeaderOrCookie(request);
        if (token == null) {
            message = "Token JWT no encontrado en la solicitud";
            throw new CustomExceptions(message);
        }

        boolean isValid = jwtProvider.validateToken(token);
        if (!isValid) {
            message = "Token JWT no válido o ha expirado";
            throw new JwtException(message);
        }

        String username = jwtProvider.getNombreUsuarioFromToken(token);
        UserInfo user = userRepository.findByUsernameAndRecordStatusTrue(username);

        if (user == null) {
            message = "Usuario no encontrado";
            throw new UsernameNotFoundException(message);
        }
        return (user);
    }

    public boolean logoutSession(HttpServletRequest request, HttpServletResponse responseHttp) {
        cookieUtils.clearTokenCookie(request, responseHttp);
        return true;
    }

}