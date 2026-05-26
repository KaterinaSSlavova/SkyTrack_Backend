package skytrack.business.useCase.user;

import skytrack.dto.user.RefreshResult;

public interface RefreshTokenUseCase {
    RefreshResult refresh(String refreshToken);
}
