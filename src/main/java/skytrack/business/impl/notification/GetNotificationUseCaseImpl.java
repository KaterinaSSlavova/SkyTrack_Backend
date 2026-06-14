package skytrack.business.impl.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.notification.NotificationNotFoundException;
import skytrack.business.exception.user.AccessDeniedException;
import skytrack.business.mapper.NotificationMapper;
import skytrack.business.service.UserService;
import skytrack.business.useCase.notification.GetNotificationUseCase;
import skytrack.dto.notification.NotificationResponse;
import skytrack.persistence.entity.NotificationEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.repo.NotificationRepository;

@Service
@RequiredArgsConstructor
public class GetNotificationUseCaseImpl implements GetNotificationUseCase {
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @Override
    public NotificationResponse getNotification(Long id) {
        UserEntity user = userService.getLoggedUser();
        NotificationEntity notification = notificationRepository
                .findById(id).orElseThrow(() -> new NotificationNotFoundException(id));
        if(!notification.getUser().getEmail().equals(user.getEmail())) {
            throw new AccessDeniedException();
        }
        return notificationMapper.toResponse(notification);
    }
}
