package skytrack.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.notification.NotificationNotFoundException;
import skytrack.business.impl.notification.GetNotificationUseCaseImpl;
import skytrack.business.mapper.NotificationMapper;
import skytrack.business.service.UserService;
import skytrack.dto.notification.NotificationResponse;
import skytrack.persistence.entity.NotificationEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.repo.NotificationRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetNotificationUseCaseImplTest {
    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private GetNotificationUseCaseImpl getNotificationUseCase;

    @Test
    void getNotification_shouldReturnNotificationResponse_whenNotificationExists() {
        UserEntity user = UserEntity.builder().id(1L).email("test@example.com").build();

        Long notificationId = 1L;

        NotificationEntity notification = new NotificationEntity();
        notification.setUser(user);

        NotificationResponse response = NotificationResponse.builder()
                .id(notificationId)
                .title("Flight delayed")
                .build();

        when(userService.getLoggedUser()).thenReturn(user);
        when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.of(notification));
        when(notificationMapper.toResponse(notification))
                .thenReturn(response);

        NotificationResponse result =
                getNotificationUseCase.getNotification(notificationId);

        assertNotNull(result);
        assertEquals(notificationId, result.getId());
        assertEquals("Flight delayed", result.getTitle());

        verify(notificationRepository).findById(notificationId);
        verify(notificationMapper).toResponse(notification);
    }

    @Test
    void getNotification_shouldThrowNotificationNotFoundException_whenNotificationDoesNotExist() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        Long notificationId = 1L;
        when(userService.getLoggedUser()).thenReturn(user);
        when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                NotificationNotFoundException.class,
                () -> getNotificationUseCase.getNotification(notificationId)
        );

        verify(notificationRepository).findById(notificationId);
        verify(notificationMapper, never()).toResponse(any());
    }
}
