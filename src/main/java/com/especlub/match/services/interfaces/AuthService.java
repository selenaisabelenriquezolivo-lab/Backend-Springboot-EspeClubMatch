package com.especlub.match.services.interfaces;

import com.especlub.match.dto.request.LoginInternalRequest;
import com.especlub.match.models.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface AuthService {
    boolean loginSession(LoginInternalRequest loginRequest, HttpServletRequest request, HttpServletResponse responseHttp);
    UserInfo validateUserJWT(HttpServletRequest request);
    boolean logoutSession(HttpServletRequest request, HttpServletResponse responseHttp);
}
