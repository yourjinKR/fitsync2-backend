package app.fitsync.domain.user.service;

import app.fitsync.domain.user.dto.UserDeleteRequest;
import app.fitsync.domain.user.dto.UserRequest;
import app.fitsync.domain.user.dto.UserResponse;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.exception.UserException;
import app.fitsync.domain.user.mapper.UserMapper;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.global.DeleteType;
import app.fitsync.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements UserServiceInterface {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(UserRequest request) {

        String loginId = request.loginId();
        Boolean existed = existUser(loginId);

        if (existed)
            throw new RestApiException(UserException.DUPLICATE_LOGINID, loginId);

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = userMapper.toEntity(request, encodedPassword);
        User createdUser = userRepository.save(user);

        Long userId = createdUser.getId();
        return new UserResponse(userId);
    }

    @Override
    public Boolean existUser(String logiId) {
        return userRepository.existsByLoginId(logiId);
    }

    @Override
    public UserResponse deleteUser(UserDeleteRequest request) {
        Long userId = request.id();
        User user = findById(userId);
        DeleteType deleteType = request.deleteType();

        if (deleteType == DeleteType.SOFT) {
            user.hide();
            userRepository.save(user);
        }

        if (deleteType == DeleteType.HARD) {
            userRepository.delete(user);
        }

        return new UserResponse(userId);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
