package skytrack.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import skytrack.business.exception.user.RefreshTokenException;
import skytrack.business.exception.user.UserNotFoundException;
import skytrack.business.service.RefreshTokenService;
import skytrack.persistence.entity.RefreshToken;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.repo.RefreshTokenRepository;
import skytrack.persistence.repo.UserRepository;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
                refreshTokenService,
                "expirationTime",
                86400000L
        );
    }

    @Test
    void createToken_shouldCreateNewRefreshToken() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .email("test@gmail.com")
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken token = refreshTokenService.createToken(1L);

        assertNotNull(token);
        assertEquals(user, token.getUser());

        verify(refreshTokenRepository).deleteByUser(user);
        verify(refreshTokenRepository).flush();
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void createToken_shouldThrow_whenUserDoesNotExist() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> refreshTokenService.createToken(1L));
    }

    @Test
    void verifyExpiration_shouldReturnToken_whenNotExpired() {
        RefreshToken token = RefreshToken.builder()
                .expiryDate(Instant.now().plusSeconds(60))
                .build();

        RefreshToken result =
                refreshTokenService.verifyExpiration(token);

        assertEquals(token, result);
    }

    @Test
    void verifyExpiration_shouldDeleteTokenAndThrow_whenExpired() {
        RefreshToken token = RefreshToken.builder()
                .expiryDate(Instant.now().minusSeconds(60))
                .build();

        assertThrows(RefreshTokenException.class,
                () -> refreshTokenService.verifyExpiration(token));

        verify(refreshTokenRepository).delete(token);
    }

    @Test
    void verifyToken_shouldReturnRefreshToken_whenTokenExists() {
        RefreshToken token = RefreshToken.builder()
                .token("abc")
                .expiryDate(Instant.now().plusSeconds(60))
                .build();

        when(refreshTokenRepository.findByToken("abc"))
                .thenReturn(Optional.of(token));

        RefreshToken result =
                refreshTokenService.verifyToken("abc");

        assertEquals(token, result);
    }

    @Test
    void verifyToken_shouldThrow_whenTokenDoesNotExist() {
        when(refreshTokenRepository.findByToken("abc"))
                .thenReturn(Optional.empty());

        assertThrows(RefreshTokenException.class,
                () -> refreshTokenService.verifyToken("abc"));
    }

    @Test
    void deleteByToken_shouldDeleteToken() {
        refreshTokenService.deleteByToken("abc");

        verify(refreshTokenRepository).deleteByToken("abc");
    }
}
