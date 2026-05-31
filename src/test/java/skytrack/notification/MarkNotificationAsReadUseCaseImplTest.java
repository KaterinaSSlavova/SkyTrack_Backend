package skytrack.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.notification.NotificationNotFoundException;
import skytrack.business.impl.notification.MarkNotificationAsReadUseCaseImpl;
import skytrack.persistence.entity.NotificationEntity;
import skytrack.persistence.repo.NotificationRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarkNotificationAsReadUseCaseImplTest {
    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private MarkNotificationAsReadUseCaseImpl markNotificationAsReadUseCase;

    @Test
    void markNotificationAsRead_shouldMarkNotificationAsRead_whenNotificationExists() {
        // Arrange
        Long notificationId = 1L;

        NotificationEntity notification = NotificationEntity.builder()
                .read(false)
                .build();

        when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.of(notification));

        // Act
        markNotificationAsReadUseCase.markNotificationAsRead(notificationId);

        // Assert
        assertTrue(notification.isRead());

        verify(notificationRepository).findById(notificationId);
        verify(notificationRepository).save(notification);
    }

    @Test
    void markNotificationAsRead_shouldThrowNotificationNotFoundException_whenNotificationDoesNotExist() {
        // Arrange
        Long notificationId = 1L;

        when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                NotificationNotFoundException.class,
                () -> markNotificationAsReadUseCase.markNotificationAsRead(notificationId)
        );

        verify(notificationRepository).findById(notificationId);
        verify(notificationRepository, never()).save(any());
    }
}
