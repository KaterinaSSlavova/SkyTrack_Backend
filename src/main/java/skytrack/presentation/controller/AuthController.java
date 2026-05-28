package skytrack.presentation.controller;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skytrack.business.exception.user.TooManyRequestsException;
import skytrack.business.useCase.user.LogInUseCase;
import skytrack.business.useCase.user.LogoutUseCase;
import skytrack.business.useCase.user.RefreshTokenUseCase;
import skytrack.business.useCase.user.RegisterUserUseCase;
import skytrack.dto.user.*;
import skytrack.presentation.security.LoginRateLimiter;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Value("${app.cors.cookieSecure}")
    private boolean cookieSecure;

    private final RegisterUserUseCase registerUserUseCase;
    private final LogInUseCase logInUseCase;
    private final LoginRateLimiter rateLimiter;
    private final LogoutUseCase logoutUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

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

        ResponseCookie jwtCookie = ResponseCookie
                .from("jwt", response.getToken())
                .httpOnly(true).secure(cookieSecure).sameSite("Strict")
                .maxAge(Duration.ofMinutes(2)).path("/").build();

        ResponseCookie refreshTokenCookie = ResponseCookie
                .from("refreshToken", response.getRefreshToken())
                .httpOnly(true).secure(cookieSecure).sameSite("Strict")
                .maxAge(Duration.ofDays(7)).path("/").build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue("refreshToken")String refreshToken,
                                       HttpServletResponse httpResponse) {
        logoutUseCase.logout(refreshToken);

        ResponseCookie jwtCookie = ResponseCookie
                .from("jwt", "")
                .httpOnly(true).secure(cookieSecure).sameSite("Strict")
                .maxAge(0).path("/").build();

        ResponseCookie refreshTokenCookie = ResponseCookie
                .from("refreshToken", "")
                .httpOnly(true).secure(cookieSecure).sameSite("Strict")
                .maxAge(0).path("/").build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(@CookieValue("refreshToken") String refreshToken,
                                        HttpServletResponse httpResponse) {
        RefreshResult result = refreshTokenUseCase.refresh(refreshToken);

        ResponseCookie jwtCookie = ResponseCookie.from("jwt", result.getToken())
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Strict")
                .maxAge(Duration.ofMinutes(2))
                .path("/")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie
                .from("refreshToken", result.getRefreshToken())
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Strict")
                .maxAge(Duration.ofDays(7))
                .path("/")
                .build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/csrf")
    public ResponseEntity<Void> csrf() {
        return ResponseEntity.ok().build();
    }
}