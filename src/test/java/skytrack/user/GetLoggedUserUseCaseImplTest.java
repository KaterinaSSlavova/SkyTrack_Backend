package skytrack.user;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.user.UserEmailInvalidException;
import skytrack.business.impl.user.GetLoggedUserUseCaseImpl;
import skytrack.dto.user.UserResponse;
import skytrack.persistence.entity.RoleEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.enumeration.Role;
import skytrack.persistence.repo.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetLoggedUserUseCaseImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetLoggedUserUseCaseImpl getLoggedUserUseCaseImpl;

    @Test
    void getLoggedUser_shouldReturnLoggedUser_whenEmailIsValid() {
        //arrange
        UserEntity user = new UserEntity(1L, "picture", "FirstName",
                "LastName", LocalDate.now().minusYears(20),
                "email@gmail.com", "HashedPass", new RoleEntity(1L, Role.PASSENGER));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        //act
        UserResponse response = getLoggedUserUseCaseImpl.getLoggedUser(user.getEmail());

        //arrange
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getFirstName(), response.getFirstName());
        assertEquals(user.getLastName(), response.getLastName());
        assertEquals(20,response.getAge());
    }

    @Test
    void getLoggedUser_shouldThrowUserEmailInvalidException_whenEmailDoesNotExist() {
        //arrange
        when(userRepository.findByEmail("email")).thenReturn(Optional.empty());

        //act and assert
        assertThrows(UserEmailInvalidException.class, () ->  getLoggedUserUseCaseImpl.getLoggedUser("email"));
    }
}
