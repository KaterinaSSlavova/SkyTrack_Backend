package skytrack.presentation.controller;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import skytrack.business.exception.user.TooManyRequestsException;
import skytrack.business.useCase.user.LogInUseCase;
import skytrack.business.useCase.user.RegisterUserUseCase;
import skytrack.dto.user.LoginUserRequest;
import skytrack.dto.user.LoginUserResponse;
import skytrack.dto.user.RegisterUserRequest;
import skytrack.dto.user.UserResponse;
import skytrack.presentation.security.LoginRateLimiter;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RegisterUserUseCase registerUserUseCase;
    private final LogInUseCase logInUseCase;
    private final LoginRateLimiter rateLimiter;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid RegisterUserRequest request) {
        UserResponse response = registerUserUseCase.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody @Valid LoginUserRequest request,
                                                   HttpServletRequest httpRequest,
                                                   HttpServletResponse httpResponse) {
        String ip = httpRequest.getRemoteAddr();
        Bucket bucket = rateLimiter.resolveBucket(ip);

        if(!bucket.tryConsume(1)){
            throw new TooManyRequestsException();
        }

        LoginUserResponse response = logInUseCase.login(request);

        ResponseCookie cookie = ResponseCookie
                .from("jwt", response.getToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .maxAge(Duration.ofMinutes(15))
                .path("/")
                .build();
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(response);
    }
}