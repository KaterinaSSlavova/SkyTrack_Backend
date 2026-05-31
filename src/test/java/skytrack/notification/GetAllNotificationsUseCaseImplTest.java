package skytrack.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.impl.notification.GetAllNotificationsUseCaseImpl;
import skytrack.business.mapper.NotificationMapper;
import skytrack.business.service.UserService;
import skytrack.dto.notification.NotificationResponse;
import skytrack.persistence.entity.NotificationEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.repo.NotificationRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAllNotificationsUseCaseImplTest {
    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserService userService;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private GetAllNotificationsUseCaseImpl getAllNotificationsUseCase;

    @Test
    void getAllNotifications_shouldReturnLoggedUserNotificationsInReverseOrder() {
        // Arrange
        UserEntity loggedUser = UserEntity.builder()
                .email("user@gmail.com")
                .build();

        UserEntity otherUser = UserEntity.builder()
                .email("other@gmail.com")
                .build();

        NotificationEntity firstNotification = NotificationEntity.builder()
                .user(loggedUser)
                .build();

        NotificationEntity secondNotification = NotificationEntity.builder()
                .user(loggedUser)
                .build();

        NotificationEntity thirdNotification = NotificationEntity.builder()
                .user(otherUser)
                .build();

        NotificationResponse firstResponse = NotificationResponse.builder()
                .id(1L)
                .build();

        NotificationResponse secondResponse = NotificationResponse.builder()
                .id(2L)
                .build();

        when(userService.getLoggedUser()).thenReturn(loggedUser);

        when(notificationRepository.findAll())
                .thenReturn(List.of(
                        firstNotification,
                        secondNotification,
                        thirdNotification
                ));

        when(notificationMapper.toResponse(firstNotification))
                .thenReturn(firstResponse);

        when(notificationMapper.toResponse(secondNotification))
                .thenReturn(secondResponse);

        // Act
        List<NotificationResponse> result =
                getAllNotificationsUseCase.getAllNotifications();

        // Assert
        assertEquals(2, result.size());

        // Reversed order
        assertEquals(2L, result.get(0).getId());
        assertEquals(1L, result.get(1).getId());

        verify(notificationMapper).toResponse(firstNotification);
        verify(notificationMapper).toResponse(secondNotification);
        verify(notificationMapper, never()).toResponse(thirdNotification);
    }

    @Test
    void getAllNotifications_shouldReturnEmptyList_whenUserHasNoNotifications() {
        // Arrange
        UserEntity loggedUser = UserEntity.builder()
                .email("user@gmail.com")
                .build();

        UserEntity otherUser = UserEntity.builder()
                .email("other@gmail.com")
                .build();

        NotificationEntity notification = NotificationEntity.builder()
                .user(otherUser)
                .build();

        when(userService.getLoggedUser()).thenReturn(loggedUser);

        when(notificationRepository.findAll())
                .thenReturn(List.of(notification));

        // Act
        List<NotificationResponse> result =
                getAllNotificationsUseCase.getAllNotifications();

        // Assert
        assertTrue(result.isEmpty());

        verify(notificationMapper, never()).toResponse(any());
    }
}
