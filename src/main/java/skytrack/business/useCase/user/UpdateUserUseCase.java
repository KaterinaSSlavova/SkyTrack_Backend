package skytrack.business.useCase.user;

import skytrack.dto.user.UpdateUserRequest;
import skytrack.dto.user.UserResponse;

public interface UpdateUserUseCase {
    UserResponse updateUser(UpdateUserRequest request);
}
