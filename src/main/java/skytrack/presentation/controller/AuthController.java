package skytrack.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import skytrack.business.useCase.user.LogInUseCase;
import skytrack.business.useCase.user.RegisterUserUseCase;
import skytrack.dto.user.LoginUserRequest;
import skytrack.dto.user.LoginUserResponse;
import skytrack.dto.user.RegisterUserRequest;
import skytrack.dto.user.UserResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RegisterUserUseCase registerUserUseCase;
    private final LogInUseCase logInUseCase;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid RegisterUserRequest request) {
        UserResponse response = registerUserUseCase.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody @Valid LoginUserRequest request) {
        LoginUserResponse response = logInUseCase.login(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
