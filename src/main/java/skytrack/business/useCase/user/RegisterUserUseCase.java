package skytrack.business.useCase.user;

import skytrack.dto.user.RegisterUserRequest;
import skytrack.dto.user.UserResponse;

public interface RegisterUserUseCase {
    UserResponse registerUser(RegisterUserRequest registerUserRequest);
}
