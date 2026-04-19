package skytrack.business.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import skytrack.business.exception.user.UserNotFoundException;
import skytrack.business.service.PasswordService;
import skytrack.business.useCase.user.DeleteUserUseCase;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.repo.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    @Override
    public void deleteUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        anonymizeUser(user);
        userRepository.save(user);
    }

    private void anonymizeUser(UserEntity user){
        user.setFirstName("Deleted");
        user.setLastName("User");
        user.setEmail("skytrack" + UUID.randomUUID().toString() + "@gmail.com");
        user.setPasswordHash(passwordService.HashPassword(UUID.randomUUID().toString()));
        user.setPicture(null);
        user.setBirthdate(null);
    }
}
