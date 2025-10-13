package com.especlub.match.services.impl;

import com.especlub.match.dto.request.LoginInternalRequest;
import com.especlub.match.dto.request.UserInfoRequestDto;
import com.especlub.match.models.UserRole;
import com.especlub.match.repositories.UserRoleRepository;
import com.especlub.match.services.interfaces.EmailService;
import com.especlub.match.shared.exceptions.CustomExceptions;
import com.especlub.match.models.UserInfo;
import com.especlub.match.repositories.UserInfoRepository;
import com.especlub.match.security.jwt.JwtProvider;
import com.especlub.match.services.interfaces.AuthService;
import com.especlub.match.shared.utils.CookieUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // injected dependencies
    private final UserInfoRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final CookieUtils cookieUtils;
    private final EmailService emailService;


    // declarate variables
    String message;

    public boolean loginSession(LoginInternalRequest loginRequest, HttpServletRequest request, HttpServletResponse responseHttp) {
        log.info("Login user: {}", loginRequest.getUsername());

        UserInfo user = null;
        if (loginRequest.getUsername() != null && !loginRequest.getUsername().isBlank()) {
            user = userRepository.findByUsernameAndRecordStatusTrue(loginRequest.getUsername());
        } else if (loginRequest.getEmail() != null && !loginRequest.getEmail().isBlank()) {
            user = userRepository.findByEmailAndRecordStatusTrue(loginRequest.getEmail());
        }
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomExceptions("Usuario, correo o contraseña incorrectos", HttpStatus.UNAUTHORIZED.value());
        }

        String jwt = jwtProvider.generateJwtByUsername(user);
        cookieUtils.addTokenCookie(request, responseHttp, jwt);

        Map<String, Object> variables = new java.util.HashMap<>();
        variables.put("username", user.getUsername());
        variables.put("loginDate", java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        variables.put("loginTime", java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        variables.put("ipAddress", request.getRemoteAddr());
        variables.put("accountActivityUrl", "https://especlubsmatch.app/login");
        emailService.sendHtmlEmail("Nuevo inicio de sesión detectado",user.getEmail(),"login-notification.html", variables);

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

    public boolean registerUserWithRoleStudent(UserInfoRequestDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new CustomExceptions("El nombre de usuario ya está registrado", HttpStatus.BAD_REQUEST.value());
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomExceptions("El correo electrónico ya está registrado", HttpStatus.BAD_REQUEST.value());
        }
        if (userRepository.existsByNationalId(dto.getNationalId())) {
            throw new CustomExceptions("La cédula ya está registrada", HttpStatus.BAD_REQUEST.value());
        }

        UserInfo user = UserInfo.builder()
            .username(dto.getUsername())
            .email(dto.getEmail())
            .phone(dto.getPhone())
            .names(dto.getNames())
            .surnames(dto.getSurnames())
            .birthDate(dto.getBirthDate())
            .acceptTerms(dto.getAcceptTerms())
            .acceptPrivacy(dto.getAcceptPrivacy())
            .nationalId(dto.getNationalId())
            .password(passwordEncoder.encode(dto.getPassword()))
            .recordStatus(true)
            .firstLogin(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        UserRole roleStudent = userRoleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("Rol ROLE_STUDENT no encontrado"));
        user.setRoles(List.of(roleStudent));

        userRepository.save(user);

        Map<String, Object> variables = new java.util.HashMap<>();
        variables.put("username", user.getUsername());
        variables.put("email", user.getEmail());
        variables.put("names", user.getNames());
        variables.put("surnames", user.getSurnames());

        emailService.sendHtmlEmail(
            "¡Cuenta creada exitosamente!",
            user.getEmail(),
            "create_account.html",
            variables
        );

        return true;
    }
}