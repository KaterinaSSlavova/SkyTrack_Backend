package skytrack.business.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.service.JwtService;
import skytrack.business.service.RefreshTokenService;
import skytrack.business.useCase.user.RefreshTokenUseCase;
import skytrack.dto.user.RefreshResult;
import skytrack.persistence.entity.RefreshToken;
import skytrack.persistence.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @Override
    public RefreshResult refresh(String refreshToken) {
        RefreshToken token = refreshTokenService.verifyToken(refreshToken);
        UserEntity user = token.getUser();

        refreshTokenService.deleteByToken(token.getToken());
        String newJWT = jwtService.generateToken(user.getEmail(), user.getRole().getRoleName().name());
        RefreshToken newRefreshToken = refreshTokenService.createToken(user.getId());

        return new RefreshResult(newJWT, newRefreshToken.getToken());
    }
}