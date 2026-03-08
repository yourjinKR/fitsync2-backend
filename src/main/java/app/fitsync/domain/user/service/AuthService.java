package app.fitsync.domain.user.service;

import app.fitsync.domain.jwt.dto.JWTResponse;
import app.fitsync.domain.jwt.service.JwtService;
import app.fitsync.domain.user.dto.LoginRequest;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.exception.UserErrorCode;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.global.exception.RestApiException;
import app.fitsync.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService implements AuthServiceInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * 자체 로그인 처리
     * 사용자 자격증명(loginId, password)을 검증하고 JWT 토큰을 발급합니다.
     *
     * @param request 로그인 요청 (아이디, 비밀번호)
     * @return JWT 응답 (Access Token, Refresh Token)
     * @throws RestApiException 사용자 미존재 또는 비밀번호 불일치 시
     */
    @Override
    @Transactional
    public JWTResponse login(LoginRequest request) {
        String loginId = request.loginId();
        String password = request.password();

        User user = userRepository.findByLoginIdAndIsSocial(loginId, false)
                .orElseThrow(() -> new RestApiException(UserErrorCode.NOT_FOUND_LOGIN_ID, loginId));

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RestApiException(UserErrorCode.INVALID_PASSWORD);
        }

        // JWT 토큰 생성
        String role = user.getRoleType().name();
        String accessToken = JwtUtil.createJWT(loginId, role, true);
        String refreshToken = JwtUtil.createJWT(loginId, role, false);

        // Refresh 토큰 저장 (화이트리스트)
        jwtService.addRefresh(loginId, refreshToken);

        return new JWTResponse(accessToken, refreshToken);
    }

    /**
     * 로그아웃 처리
     * 현재 인증된 사용자의 Refresh 토큰을 삭제하여 로그아웃 상태로 변경합니다.
     * 클라이언트에서도 localStorage의 토큰들을 제거해야 합니다.
     *
     * @throws IllegalStateException 인증되지 않은 사용자인 경우
     */
    @Override
    @Transactional
    public void logout() {
        // 현재 인증된 사용자의 loginId 획득
        String loginId = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        // 해당 사용자의 모든 Refresh 토큰 삭제 (화이트리스트에서 제거)
        jwtService.removeRefreshUser(loginId);
    }
}
