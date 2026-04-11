package skytrack.business.useCase.user;

import skytrack.dto.user.LoginUserRequest;
import skytrack.dto.user.LoginUserResponse;

public interface LogInUseCase {
    LoginUserResponse login(LoginUserRequest loginUserRequest);
}
