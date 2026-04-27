package skytrack.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import skytrack.business.exception.user.UserNotFoundException;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.repo.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserEntity getLoggedUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}
