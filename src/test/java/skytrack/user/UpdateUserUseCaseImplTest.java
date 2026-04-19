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
import skytrack.business.impl.user.UpdateUserUseCaseImpl;
import skytrack.dto.user.UpdateUserRequest;
import skytrack.dto.user.UserResponse;
import skytrack.persistence.entity.RoleEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.enumeration.Role;
import skytrack.persistence.repo.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUserUseCaseImpl updateUserUseCaseImpl;

    @BeforeEach
    void setUp() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("old@gmail.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void updateUser_shouldReturnUpdatedUser_whenUserExists(){
        //arrange
        UserEntity user = new UserEntity(1L, "old-picture", "OldFirst",
                "OldLast", LocalDate.of(2000, 1, 1),
                "old@gmail.com", "hashedPassword", new RoleEntity(1L, Role.PASSENGER));
        UpdateUserRequest request = new UpdateUserRequest(null,"NewFirstName", null, null, null);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //act
        UserResponse response = updateUserUseCaseImpl.updateUser(request);

        //assert
        assertNotNull(response);
        assertEquals("NewFirstName", user.getFirstName());
        assertEquals("OldLast", user.getLastName());
        assertEquals("old@gmail.com", user.getEmail());
        assertEquals("old-picture", user.getPicture());
        assertEquals(LocalDate.of(2000, 1, 1), user.getBirthdate());
    }
}
