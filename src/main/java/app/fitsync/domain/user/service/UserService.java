package app.fitsync.domain.user.service;

import app.fitsync.domain.user.dto.UserRequest;
import app.fitsync.domain.user.dto.UserResponse;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.mapper.UserMapper;
import app.fitsync.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(UserRequest request) {
        User user = userMapper.toEntity(request);
        User createdUser = userRepository.save(user);
        Long userId = createdUser.getId();
        return new UserResponse(userId);
    }
}
