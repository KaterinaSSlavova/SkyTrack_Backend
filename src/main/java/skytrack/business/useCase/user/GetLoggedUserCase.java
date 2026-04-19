package skytrack.business.useCase.user;

import skytrack.dto.user.UserResponse;

public interface GetLoggedUserCase {
    UserResponse getLoggedUser(String email);
}
