package app.fitsync.domain.user.service;

import app.fitsync.domain.jwt.dto.JWTResponse;
import app.fitsync.domain.user.dto.LoginRequest;

public interface AuthServiceInterface {
    /**
     * 자체 로그인 처리
     * @param request 로그인 요청 (아이디, 비밀번호)
     * @return JWT 토큰 (Access Token, Refresh Token)
     */
    JWTResponse login(LoginRequest request);

    /**
     * 로그아웃 처리
     * 현재 사용자의 Refresh 토큰을 삭제하여 로그아웃 상태로 변경
     */
    void logout();
}
