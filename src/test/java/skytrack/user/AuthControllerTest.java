package skytrack.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import skytrack.business.service.JwtService;
import skytrack.business.useCase.user.LogInUseCase;
import skytrack.business.useCase.user.LogoutUseCase;
import skytrack.business.useCase.user.RefreshTokenUseCase;
import skytrack.business.useCase.user.RegisterUserUseCase;
import skytrack.dto.user.*;
import skytrack.presentation.controller.AuthController;
import skytrack.presentation.security.JwtAuthenticationFilter;
import skytrack.presentation.security.LoginRateLimiter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CacheManager cacheManager;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;

    @MockitoBean
    private LogInUseCase logInUseCase;

    @MockitoBean
    private LoginRateLimiter rateLimiter;

    @MockitoBean
    private LogoutUseCase logoutUseCase;

    @MockitoBean
    private RefreshTokenUseCase refreshTokenUseCase;

    private Bucket bucket;

    @BeforeEach
    void setupFilter() throws Exception {
        doAnswer(invocation -> {
            jakarta.servlet.FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());

        bucket = mock(Bucket.class);
        when(rateLimiter.resolveBucket(anyString())).thenReturn(bucket);
    }

    @Test
    void register_shouldReturn201() throws Exception {
        RegisterUserRequest request = RegisterUserRequest.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@example.com")
                .password("secret123")
                .birthDate(java.time.LocalDate.of(1990, 1, 1))
                .build();

        UserResponse response = UserResponse.builder()
                .id(1L)
                .email("jane@example.com")
                .build();

        when(registerUserUseCase.registerUser(any(RegisterUserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(registerUserUseCase).registerUser(any(RegisterUserRequest.class));
    }

    @Test
    void login_shouldReturn200AndSetCookies() throws Exception {
        LoginUserRequest request = new LoginUserRequest("jane@example.com", "secret123");

        LoginUserResponse response = LoginUserResponse.builder()
                .token("jwt-token")
                .refreshToken("refresh-token")
                .build();

        when(bucket.tryConsume(1)).thenReturn(true);
        when(logInUseCase.login(any(LoginUserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"));

        verify(logInUseCase).login(any(LoginUserRequest.class));
    }

    @Test
    void login_rateLimitExceeded_shouldReturn429() throws Exception {
        LoginUserRequest request = new LoginUserRequest("jane@example.com", "secret123");

        when(bucket.tryConsume(1)).thenReturn(false);

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isTooManyRequests());

        verifyNoInteractions(logInUseCase);
    }

    @Test
    @WithMockUser
    void logout_shouldReturn204AndClearCookies() throws Exception {
        mockMvc.perform(post("/auth/logout")
                        .cookie(new jakarta.servlet.http.Cookie("refreshToken", "some-refresh-token")))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("Set-Cookie"));

        verify(logoutUseCase).logout("some-refresh-token");
    }

    @Test
    @WithMockUser
    void refresh_shouldReturn204AndSetCookies() throws Exception {
        RefreshResult result = RefreshResult.builder()
                .token("new-jwt")
                .refreshToken("new-refresh")
                .build();

        when(refreshTokenUseCase.refresh("some-refresh-token")).thenReturn(result);

        mockMvc.perform(post("/auth/refresh")
                        .cookie(new jakarta.servlet.http.Cookie("refreshToken", "some-refresh-token")))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("Set-Cookie"));

        verify(refreshTokenUseCase).refresh("some-refresh-token");
    }

    @Test
    void csrf_shouldReturn200() throws Exception {
        mockMvc.perform(get("/auth/csrf"))
                .andExpect(status().isOk());
    }
}