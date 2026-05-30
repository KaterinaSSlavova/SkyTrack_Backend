package skytrack.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.notification.NotificationNotFoundException;
import skytrack.business.impl.notification.GetNotificationUseCaseImpl;
import skytrack.business.mapper.NotificationMapper;
import skytrack.dto.notification.NotificationResponse;
import skytrack.persistence.entity.NotificationEntity;
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

    @InjectMocks
    private GetNotificationUseCaseImpl getNotificationUseCase;

    @Test
    void getNotification_shouldReturnNotificationResponse_whenNotificationExists() {
        // Arrange
        Long notificationId = 1L;

        NotificationEntity notification = new NotificationEntity();

        NotificationResponse response = NotificationResponse.builder()
                .id(notificationId)
                .title("Flight delayed")
                .build();

        when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.of(notification));

        when(notificationMapper.toResponse(notification))
                .thenReturn(response);

        // Act
        NotificationResponse result =
                getNotificationUseCase.getNotification(notificationId);

        // Assert
        assertNotNull(result);
        assertEquals(notificationId, result.getId());
        assertEquals("Flight delayed", result.getTitle());

        verify(notificationRepository).findById(notificationId);
        verify(notificationMapper).toResponse(notification);
    }

    @Test
    void getNotification_shouldThrowNotificationNotFoundException_whenNotificationDoesNotExist() {
        // Arrange
        Long notificationId = 1L;

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
