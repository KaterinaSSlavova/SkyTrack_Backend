package skytrack.business.useCase.notification;

import skytrack.dto.notification.NotificationResponse;

public interface GetNotificationUseCase {
    NotificationResponse getNotification(Long id);
}
