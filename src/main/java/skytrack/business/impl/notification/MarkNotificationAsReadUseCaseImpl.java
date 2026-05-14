package skytrack.business.impl.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.notification.NotificationNotFoundException;
import skytrack.business.useCase.notification.MarkNotificationAsReadUseCase;
import skytrack.persistence.entity.NotificationEntity;
import skytrack.persistence.repo.NotificationRepository;

@Service
@RequiredArgsConstructor
public class MarkNotificationAsReadUseCaseImpl implements MarkNotificationAsReadUseCase {
    private final NotificationRepository notificationRepository;

    @Override
    public void markNotificationAsRead(Long id) {
        NotificationEntity notification =  notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
