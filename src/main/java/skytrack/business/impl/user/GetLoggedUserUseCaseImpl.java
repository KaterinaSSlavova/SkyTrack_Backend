package skytrack.business.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.user.UserEmailInvalidException;
import skytrack.business.mapper.UserMapper;
import skytrack.business.useCase.user.GetLoggedUserCase;
import skytrack.dto.user.UserResponse;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.repo.UserRepository;

@Service
@RequiredArgsConstructor
public class GetLoggedUserUseCaseImpl implements GetLoggedUserCase {
    private final UserRepository userRepository;

    @Override
    public UserResponse getLoggedUser(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailInvalidException(email));

        return UserMapper.toResponse(user);
    }
}
