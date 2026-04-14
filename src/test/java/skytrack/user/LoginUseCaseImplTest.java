package skytrack.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.user.UserEmailInvalidException;
import skytrack.business.exception.user.UserPasswordInvalidException;
import skytrack.business.impl.user.LoginUseCaseImpl;
import skytrack.business.service.JwtService;
import skytrack.business.service.PasswordService;
import skytrack.dto.user.LoginUserRequest;
import skytrack.dto.user.LoginUserResponse;
import skytrack.persistence.entity.RoleEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.enumeration.Role;
import skytrack.persistence.repo.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginUseCaseImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordService passwordService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginUseCaseImpl loginUseCaseImpl;

    @Test
    public void login_shouldReturnLoginResponse_whenUserIsValid() {
        //arrange
        LoginUserRequest request = new LoginUserRequest("email@gmail.com", "password");
        UserEntity user = new UserEntity(1L, "picture", "FirstName",
                "LastName", LocalDate.now().minusYears(20),
                "email@gmail.com", "HashedPass", new RoleEntity(1L, Role.PASSENGER));
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordService.checkPassword(request.getPassword(), user.getPasswordHash())).thenReturn(true);
        when(jwtService.generateToken(user.getEmail(), user.getRole().getRoleName().name())).thenReturn("token");

        //act
        LoginUserResponse response = loginUseCaseImpl.login(request);

        //assert
        assertEquals("token",  response.getToken());
        assertEquals(user.getFirstName(), response.getFirstName());
        assertEquals(user.getRole().getRoleName().name(), response.getRole());
    }

    @Test
    public void login_shouldThrowEmailInvalidException_whenEmailDoesNotExist() {
        //arrange
        LoginUserRequest request = new LoginUserRequest("email@gmail.com", "password");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        //act and assert
        assertThrows(UserEmailInvalidException.class, () -> loginUseCaseImpl.login(request));
    }

    @Test
    public void login_shouldThrowUserPasswordInvalidException_whenPasswordDoesNotMatch() {
        //arrange
        LoginUserRequest request = new LoginUserRequest("email@gmail.com", "password");
        UserEntity user = new UserEntity(1L, "picture", "FirstName",
                "LastName", LocalDate.now().minusYears(20),
                "email@gmail.com", "HashedPass", new RoleEntity(1L, Role.PASSENGER));
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordService.checkPassword(request.getPassword(), user.getPasswordHash())).thenReturn(false);

        //act and assert
        assertThrows(UserPasswordInvalidException.class, () -> loginUseCaseImpl.login(request));
    }
}
