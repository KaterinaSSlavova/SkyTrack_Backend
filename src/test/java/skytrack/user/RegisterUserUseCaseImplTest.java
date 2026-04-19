package skytrack.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.user.UserEmailAlreadyExistsException;
import skytrack.business.exception.user.UserTooYoungException;
import skytrack.business.impl.user.RegisterUserUseCaseImpl;
import skytrack.business.service.PasswordService;
import skytrack.dto.user.RegisterUserRequest;
import skytrack.dto.user.UserResponse;
import skytrack.persistence.entity.RoleEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.enumeration.Role;
import skytrack.persistence.repo.RoleRepository;
import skytrack.persistence.repo.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RegisterUserUseCaseImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordService passwordService;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RegisterUserUseCaseImpl registerUserUseCaseImpl;

    @Test
    public void registerUser_shouldReturnRegisteredUser_whenUserEmailUniqueAndUserAgeIsValid(){
        //arrange
        RoleEntity role = new RoleEntity(1L, Role.PASSENGER);
        UserEntity user = new UserEntity(1L, "picture", "FirstName",
                "LastName", LocalDate.now().minusYears(20),
                "userEmail@gmail.com", "HashedPass", role);
        RegisterUserRequest request = new RegisterUserRequest("picture", "FirstName",
                "LastName", LocalDate.now().minusYears(20),
                "userEmail@gmail.com", "Pass");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordService.HashPassword(request.getPassword())).thenReturn(user.getPasswordHash());
        when(roleRepository.findByRoleName(role.getRoleName())).thenReturn(Optional.of(role));
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        //act
        UserResponse response = registerUserUseCaseImpl.registerUser(request);

        //assert
        assertNotNull(response);
        assertEquals(user.getFirstName(),response.getFirstName());
        assertEquals(user.getLastName(), response.getLastName());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(20,response.getAge());
    }

    @Test
    public void registerUser_shouldThrowEmailAlreadyExistsException_whenUserEmailExists(){
        //arrange
        RegisterUserRequest request = new RegisterUserRequest("picture", "FirstName",
                "LastName", LocalDate.now().minusYears(20),
                "userEmail@gmail.com", "Pass");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // act and assert
        assertThrows(UserEmailAlreadyExistsException.class, () -> registerUserUseCaseImpl.registerUser(request));
    }

    @Test
    public void registerUser_shouldThrowUserTooYoungException_whenUserIsUnder16(){
        //arrange
        RegisterUserRequest request = new RegisterUserRequest("picture", "FirstName",
                "LastName", LocalDate.now().minusYears(10),
                "userEmail@gmail.com", "Pass");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);

        // act and assert
        assertThrows(UserTooYoungException.class, () -> registerUserUseCaseImpl.registerUser(request));
    }
}
