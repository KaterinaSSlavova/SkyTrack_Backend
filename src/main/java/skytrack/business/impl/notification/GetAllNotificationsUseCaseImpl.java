package skytrack.business.impl.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.mapper.NotificationMapper;
import skytrack.business.service.UserService;
import skytrack.business.useCase.notification.GetAllNotificationsUseCase;
import skytrack.dto.notification.NotificationResponse;
import skytrack.persistence.repo.NotificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllNotificationsUseCaseImpl implements GetAllNotificationsUseCase {
    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationResponse> getAllNotifications() {
        String email = userService.getLoggedUser().getEmail();
        return notificationRepository.findAll().stream()
                .filter(u -> u.getUser().getEmail().equals(email))
                .map(notificationMapper::toResponse).toList();
    }
}