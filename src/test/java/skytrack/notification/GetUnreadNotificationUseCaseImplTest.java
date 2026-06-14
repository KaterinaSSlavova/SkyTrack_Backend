package skytrack.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.impl.notification.GetUnreadNotificationsUseCaseImpl;
import skytrack.business.mapper.NotificationMapper;
import skytrack.business.service.UserService;
import skytrack.dto.notification.NotificationResponse;
import skytrack.persistence.entity.NotificationEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.repo.NotificationRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUnreadNotificationUseCaseImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private GetUnreadNotificationsUseCaseImpl useCase;

    @Test
    void getUnreadNotifications_shouldReturnMappedNotifications() {
        UserEntity user = new UserEntity();
        user.setEmail("jane@example.com");

        NotificationEntity entity = new NotificationEntity();
        NotificationResponse response = NotificationResponse.builder().id(1L).build();

        when(userService.getLoggedUser()).thenReturn(user);
        when(notificationRepository.findByUserEmailAndReadFalse("jane@example.com"))
                .thenReturn(List.of(entity));
        when(notificationMapper.toResponse(entity)).thenReturn(response);

        List<NotificationResponse> result = useCase.getUnreadNotifications();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(notificationRepository).findByUserEmailAndReadFalse("jane@example.com");
        verify(notificationMapper).toResponse(entity);
    }

    @Test
    void getUnreadNotifications_noUnread_shouldReturnEmptyList() {
        UserEntity user = new UserEntity();
        user.setEmail("jane@example.com");

        when(userService.getLoggedUser()).thenReturn(user);
        when(notificationRepository.findByUserEmailAndReadFalse("jane@example.com"))
                .thenReturn(List.of());

        List<NotificationResponse> result = useCase.getUnreadNotifications();

        assertThat(result).isEmpty();
        verify(notificationMapper, never()).toResponse(any());
    }
}
