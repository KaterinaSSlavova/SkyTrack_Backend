package skytrack.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.user.RefreshTokenException;
import skytrack.business.impl.user.RefreshTokenUseCaseImpl;
import skytrack.business.service.JwtService;
import skytrack.business.service.RefreshTokenService;
import skytrack.dto.user.RefreshResult;
import skytrack.persistence.entity.RefreshToken;
import skytrack.persistence.entity.RoleEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.enumeration.Role;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenUseCaseImplTest {
    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private RefreshTokenUseCaseImpl refreshTokenUseCase;

    @Test
    void refresh_shouldReturnNewJwtAndRefreshToken_whenRefreshTokenIsValid() {
        UserEntity user = new UserEntity(
                1L,
                "picture",
                "FirstName",
                "LastName",
                LocalDate.now().minusYears(20),
                "email@gmail.com",
                "hashedPassword",
                new RoleEntity(1L, Role.PASSENGER)
        );

        RefreshToken oldRefreshToken = RefreshToken.builder()
                .token("old-refresh-token")
                .user(user)
                .build();

        RefreshToken newRefreshToken = RefreshToken.builder()
                .token("new-refresh-token")
                .user(user)
                .build();

        when(refreshTokenService.verifyToken("old-refresh-token"))
                .thenReturn(oldRefreshToken);

        when(jwtService.generateToken("email@gmail.com", "PASSENGER"))
                .thenReturn("new-jwt-token");

        when(refreshTokenService.createToken(1L))
                .thenReturn(newRefreshToken);

        RefreshResult result =
                refreshTokenUseCase.refresh("old-refresh-token");

        assertEquals("new-jwt-token", result.getToken());
        assertEquals("new-refresh-token", result.getRefreshToken());

        verify(refreshTokenService).verifyToken("old-refresh-token");
        verify(refreshTokenService).deleteByToken("old-refresh-token");
        verify(jwtService).generateToken("email@gmail.com", "PASSENGER");
        verify(refreshTokenService).createToken(1L);
    }

    @Test
    void refresh_shouldThrowRefreshTokenException_whenRefreshTokenIsInvalid() {
        when(refreshTokenService.verifyToken("invalid-token"))
                .thenThrow(new RefreshTokenException());

        assertThrows(RefreshTokenException.class,
                () -> refreshTokenUseCase.refresh("invalid-token"));

        verify(refreshTokenService).verifyToken("invalid-token");
        verify(refreshTokenService, never()).deleteByToken(anyString());
        verify(jwtService, never()).generateToken(anyString(), anyString());
        verify(refreshTokenService, never()).createToken(anyLong());
    }
}
