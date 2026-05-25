package skytrack.business.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skytrack.business.service.RefreshTokenService;
import skytrack.business.useCase.user.LogoutUseCase;

@Service
@RequiredArgsConstructor
public class LogoutUseCaseImpl implements LogoutUseCase {
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public void logout(String token) {
        refreshTokenService.deleteByToken(token);
    }
}
