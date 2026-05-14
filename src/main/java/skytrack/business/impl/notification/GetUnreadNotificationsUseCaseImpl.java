package skytrack.business.impl.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.mapper.NotificationMapper;
import skytrack.business.service.UserService;
import skytrack.business.useCase.notification.GetUnreadNotificationsUseCase;
import skytrack.dto.notification.NotificationResponse;
import skytrack.persistence.repo.NotificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUnreadNotificationsUseCaseImpl implements GetUnreadNotificationsUseCase {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserService userService;

    @Override
    public List<NotificationResponse> getUnreadNotifications() {
        String email = userService.getLoggedUser().getEmail();
        return notificationRepository.findByUserEmailAndReadFalse(email)
                .stream().map(notificationMapper::toResponse).toList();
    }
}