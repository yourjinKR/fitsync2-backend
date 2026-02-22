package app.fitsync.domain.user.service;

import app.fitsync.domain.user.dto.UserRequest;
import app.fitsync.domain.user.dto.UserResponse;
import app.fitsync.global.DeleteType;

public interface UserServiceInterface {
    UserResponse createUser(UserRequest request);
    Boolean existUser(String logiId);
    UserResponse findMe();
    UserResponse deleteMe(DeleteType deleteType);
}
