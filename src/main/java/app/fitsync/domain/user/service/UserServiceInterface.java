package app.fitsync.domain.user.service;

import app.fitsync.domain.user.dto.UserRequest;
import app.fitsync.domain.user.dto.UserResponse;

public interface UserServiceInterface {
    UserResponse createUser(UserRequest request);
}
