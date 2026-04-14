package skytrack.business.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import skytrack.business.exception.user.UserNotFoundException;
import skytrack.business.exception.user.UserTooOldException;
import skytrack.business.exception.user.UserTooYoungException;
import skytrack.business.mapper.UserMapper;
import skytrack.business.useCase.user.UpdateUserUseCase;
import skytrack.dto.user.UpdateUserRequest;
import skytrack.dto.user.UserResponse;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.repo.UserRepository;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private final UserRepository userRepository;

    @Override
    public UserResponse updateUser(UpdateUserRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if(request.getPicture()!=null){
            user.setPicture(request.getPicture());
        }

        if(request.getFirstName() !=null){
            user.setFirstName(request.getFirstName());
        }

        if(request.getLastName() !=null){
            user.setLastName(request.getLastName());
        }

        if(request.getEmail() !=null){
            user.setEmail(request.getEmail());
        }

        if(request.getBirthDate() !=null){
            int age = Period.between(request.getBirthDate(), LocalDate.now()).getYears();
            if(age < 16) throw new UserTooYoungException();
            if(age > 120) throw new UserTooOldException();
            user.setBirthdate(request.getBirthDate());
        }

        return UserMapper.toResponse(userRepository.save(user));
    }
}