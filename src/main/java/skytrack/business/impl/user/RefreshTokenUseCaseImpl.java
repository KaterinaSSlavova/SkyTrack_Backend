package skytrack.business.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.service.JwtService;
import skytrack.business.service.RefreshTokenService;
import skytrack.business.useCase.user.RefreshTokenUseCase;
import skytrack.persistence.entity.RefreshToken;
import skytrack.persistence.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @Override
    public String refresh(String refreshToken) {
        RefreshToken token = refreshTokenService.verifyToken(refreshToken);
        UserEntity user = token.getUser();

        return jwtService.generateToken(user.getEmail(), user.getRole().getRoleName().name());
    }
}