package app.fitsync.domain.user.service;

import app.fitsync.domain.jwt.dto.JWTResponse;
import app.fitsync.domain.jwt.service.JwtService;
import app.fitsync.domain.user.dto.LoginRequest;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.exception.UserException;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.global.exception.RestApiException;
import app.fitsync.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService implements AuthServiceInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public JWTResponse login(LoginRequest request) {
        String loginId = request.loginId();
        String password = request.password();

        // 자체 로그인 사용자 찾기
        User user = userRepository.findByLoginIdAndIsSocial(loginId, false)
                .orElseThrow(() -> new RestApiException(UserException.NOT_FOUND_LOGIN_ID, loginId));

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RestApiException(UserException.NOT_FOUND_LOGIN_ID, loginId);
        }

        // JWT 토큰 생성
        String role = user.getRoleType().name();
        String accessToken = JwtUtil.createJWT(loginId, role, true);
        String refreshToken = JwtUtil.createJWT(loginId, role, false);

        // Refresh 토큰 저장
        jwtService.addRefresh(loginId, refreshToken);

        return new JWTResponse(accessToken, refreshToken);
    }
}
