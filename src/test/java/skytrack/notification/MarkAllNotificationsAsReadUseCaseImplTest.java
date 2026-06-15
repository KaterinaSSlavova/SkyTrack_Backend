package skytrack.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.impl.notification.MarkAllNotificationsAsReadUseCaseImpl;
import skytrack.business.service.UserService;
import skytrack.persistence.entity.NotificationEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.repo.NotificationRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MarkAllNotificationsAsReadUseCaseImplTest {
    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MarkAllNotificationsAsReadUseCaseImpl useCase;

    @Test
    void markAllNotificationsAsRead_shouldMarkNotificationsAsRead() {
        // Arrange
        UserEntity user = UserEntity.builder()
                .email("test@gmail.com")
                .build();

        NotificationEntity notification1 = NotificationEntity.builder()
                .read(false)
                .build();

        NotificationEntity notification2 = NotificationEntity.builder()
                .read(false)
                .build();

        when(userService.getLoggedUser()).thenReturn(user);

        when(notificationRepository.findByUserEmailAndReadFalse("test@gmail.com"))
                .thenReturn(List.of(notification1, notification2));

        // Act
        useCase.markAllNotificationsAsRead();

        // Assert
        assertTrue(notification1.isRead());
        assertTrue(notification2.isRead());

        verify(notificationRepository)
                .saveAll(List.of(notification1, notification2));
    }

    @Test
    void markAllNotificationsAsRead_shouldSaveEmptyList_whenNoNotificationsExist() {
        // Arrange
        UserEntity user = UserEntity.builder()
                .email("test@gmail.com")
                .build();

        when(userService.getLoggedUser()).thenReturn(user);

        when(notificationRepository.findByUserEmailAndReadFalse("test@gmail.com"))
                .thenReturn(List.of());

        // Act
        useCase.markAllNotificationsAsRead();

        // Assert
        verify(notificationRepository).saveAll(List.of());
    }
}
