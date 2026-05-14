package skytrack.business.impl.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.service.UserService;
import skytrack.business.useCase.notification.MarkAllNotificationsAsReadUseCase;
import skytrack.persistence.entity.NotificationEntity;
import skytrack.persistence.repo.NotificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkAllNotificationsAsReadUseCaseImpl implements MarkAllNotificationsAsReadUseCase {
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @Override
    public void markAllNotificationsAsRead() {
        String email = userService.getLoggedUser().getEmail();
        List<NotificationEntity> notifications = notificationRepository.findByUserEmailAndReadFalse(email);
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
    }
}