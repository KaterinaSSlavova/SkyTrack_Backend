package skytrack.business.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.user.RoleNotFoundException;
import skytrack.business.exception.user.UserEmailAlreadyExistsException;
import skytrack.business.exception.user.UserTooOldException;
import skytrack.business.exception.user.UserTooYoungException;
import skytrack.business.mapper.UserMapper;
import skytrack.business.service.PasswordService;
import skytrack.business.useCase.user.RegisterUserUseCase;
import skytrack.dto.user.RegisterUserRequest;
import skytrack.dto.user.UserResponse;
import skytrack.persistence.entity.RoleEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.enumeration.Role;
import skytrack.persistence.repo.RoleRepository;
import skytrack.persistence.repo.UserRepository;

import java.time.LocalDate;
import java.time.Period;


@Service
@RequiredArgsConstructor
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final RoleRepository roleRepository;

    @Override
    public UserResponse registerUser(RegisterUserRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) throw new UserEmailAlreadyExistsException(request.getEmail());
        int age = Period.between(request.getBirthDate(), LocalDate.now()).getYears();
        if(age < 16) throw new UserTooYoungException();
        if(age > 120) throw new UserTooOldException();

        UserEntity user = UserMapper.toEntity(request);
        user.setPasswordHash(passwordService.HashPassword(request.getPassword()));
        user.setRole(assignRole());
        UserEntity registeredUser = userRepository.save(user);
        return UserMapper.toResponse(registeredUser);
    }

    private RoleEntity assignRole() {
        return roleRepository.findByRoleName(Role.PASSENGER)
                .orElseThrow(() -> new RoleNotFoundException(Role.PASSENGER.name()));
    }
}