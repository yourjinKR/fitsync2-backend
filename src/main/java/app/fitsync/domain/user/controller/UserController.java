package app.fitsync.domain.user.controller;

import app.fitsync.domain.user.dto.UserDeleteRequest;
import app.fitsync.domain.user.dto.UserRequest;
import app.fitsync.domain.user.dto.UserResponse;
import app.fitsync.domain.user.service.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserServiceInterface userService;

    @PostMapping("/api/user")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/exist/{loginId}")
    public ResponseEntity<Boolean> existUser(@PathVariable String loginId) {
        Boolean result = userService.existUser(loginId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/api/user/me")
    public ResponseEntity<UserResponse> findMe() {
        UserResponse me = userService.findMe();
        return ResponseEntity.ok(me);
    }

    @DeleteMapping("/api/user")
    public ResponseEntity<UserResponse> deleteUser(@RequestBody UserDeleteRequest request) {
        UserResponse response = userService.deleteUser(request);
        return ResponseEntity.ok(response);
    }
}
