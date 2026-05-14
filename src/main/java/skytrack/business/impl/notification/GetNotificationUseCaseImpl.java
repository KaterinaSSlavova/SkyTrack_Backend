package skytrack.business.impl.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.notification.NotificationNotFoundException;
import skytrack.business.mapper.NotificationMapper;
import skytrack.business.useCase.notification.GetNotificationUseCase;
import skytrack.dto.notification.NotificationResponse;
import skytrack.persistence.repo.NotificationRepository;

@Service
@RequiredArgsConstructor
public class GetNotificationUseCaseImpl implements GetNotificationUseCase {
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;

    @Override
    public NotificationResponse getNotification(Long id) {
        return notificationMapper.toResponse(notificationRepository
                .findById(id).orElseThrow(() -> new NotificationNotFoundException(id)));
    }
}
