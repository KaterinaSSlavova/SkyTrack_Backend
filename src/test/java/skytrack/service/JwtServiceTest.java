package skytrack.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import skytrack.business.service.JwtService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService,
                "secret",
                "myVerySecretKeyForJwtTesting12345678901234567890"
        );

        ReflectionTestUtils.setField(
                jwtService,
                "expirationMs",
                3600000L
        );

        jwtService.init();
    }

    @Test
    void generateToken_shouldCreateValidToken() {
        String token =
                jwtService.generateToken("test@gmail.com", "PASSENGER");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extractEmailFromToken_shouldReturnCorrectEmail() {
        String token =
                jwtService.generateToken("test@gmail.com", "PASSENGER");

        String email =
                jwtService.extractEmailFromToken(token);

        assertEquals("test@gmail.com", email);
    }

    @Test
    void extractRoleFromToken_shouldReturnCorrectRole() {
        String token =
                jwtService.generateToken("test@gmail.com", "PASSENGER");

        String role =
                jwtService.extractRoleFromToken(token);

        assertEquals("PASSENGER", role);
    }

    @Test
    void isTokenValid_shouldReturnTrue_whenTokenIsValid() {
        String token =
                jwtService.generateToken("test@gmail.com", "PASSENGER");

        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenTokenIsInvalid() {
        assertFalse(jwtService.isTokenValid("invalid-token"));
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenTokenIsExpired()
            throws InterruptedException {

        JwtService shortLivedJwtService = new JwtService();

        ReflectionTestUtils.setField(
                shortLivedJwtService,
                "secret",
                "myVerySecretKeyForJwtTesting12345678901234567890"
        );

        ReflectionTestUtils.setField(
                shortLivedJwtService,
                "expirationMs",
                1L
        );

        shortLivedJwtService.init();

        String token =
                shortLivedJwtService.generateToken(
                        "test@gmail.com",
                        "PASSENGER"
                );

        Thread.sleep(10);

        assertFalse(shortLivedJwtService.isTokenValid(token));
    }
}
