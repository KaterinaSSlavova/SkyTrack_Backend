package skytrack.business.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.user.UserEmailInvalidException;
import skytrack.business.exception.user.UserPasswordInvalidException;
import skytrack.business.service.JwtService;
import skytrack.business.service.PasswordService;
import skytrack.business.useCase.user.LogInUseCase;
import skytrack.dto.user.LoginUserRequest;
import skytrack.dto.user.LoginUserResponse;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.repo.UserRepository;

@Service
@RequiredArgsConstructor
public class LoginUseCaseImpl implements LogInUseCase {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final JwtService jwtService;

    @Override
    public LoginUserResponse login(LoginUserRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserEmailInvalidException(request.getEmail()));

        if(!passwordService.checkPassword(request.getPassword(), user.getPasswordHash())) {
            throw new UserPasswordInvalidException();
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().getRoleName().name());
        return new LoginUserResponse(token, user.getId(), user.getFirstName(), user.getRole().getRoleName().name());
    }

}