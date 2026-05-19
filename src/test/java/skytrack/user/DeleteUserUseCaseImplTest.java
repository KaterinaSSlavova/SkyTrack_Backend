package skytrack.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import skytrack.business.exception.user.UserNotFoundException;
import skytrack.business.impl.user.DeleteUserUseCaseImpl;
import skytrack.business.service.PasswordService;
import skytrack.persistence.entity.RoleEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.enumeration.Role;
import skytrack.persistence.repo.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteUserUseCaseImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private DeleteUserUseCaseImpl deleteUserUseCaseImpl;

    @BeforeEach
    void setUp() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("email@gmail.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
     void deleteUser_shouldAnonymizeUser_whenUserExists() {
        //arrange
        UserEntity user = new UserEntity(1L, "picture", "FirstName",
                "LastName", LocalDate.now().minusYears(20),
                "email@gmail.com", "HashedPass", new RoleEntity(1L, Role.PASSENGER));

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordService.HashPassword(anyString())).thenReturn("random");

        //act
        deleteUserUseCaseImpl.deleteUser();

        //assert
        assertEquals("Deleted", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertNull(user.getPicture());
        assertNull(user.getBirthdate());
        assertEquals("random", user.getPasswordHash());
    }

    @Test
     void deleteUser_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        //arrange
        when(userRepository.findByEmail("email@gmail.com")).thenReturn(Optional.empty());

        //act and assert
        assertThrows(UserNotFoundException.class, () -> deleteUserUseCaseImpl.deleteUser());
    }
}
