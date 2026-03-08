package app.fitsync.domain.user;

import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.exception.UserErrorCode;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.global.exception.RestApiException;
import app.fitsync.global.security.SecurityFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final SecurityFacade securityFacade;
    private final UserRepository userRepository;

    @Transactional
    public User getUser() {

        String loginId = securityFacade.getLoginIdOrThrow();

        return userRepository.findByLoginIdAndHiddenIsFalse(loginId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.NOT_FOUND_LOGIN_ID, loginId));

    }

    @Transactional
    public Long getUserId() {
        return getUser().getId();
    }
}
