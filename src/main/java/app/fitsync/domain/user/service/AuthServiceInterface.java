package app.fitsync.domain.user.service;

import app.fitsync.domain.jwt.dto.JWTResponse;
import app.fitsync.domain.user.dto.LoginRequest;

public interface AuthServiceInterface {
    JWTResponse login(LoginRequest request);
}
