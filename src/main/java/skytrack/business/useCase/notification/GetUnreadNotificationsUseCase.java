package skytrack.business.useCase.notification;

import skytrack.dto.notification.NotificationResponse;

import java.util.List;

public interface GetUnreadNotificationsUseCase {
    List<NotificationResponse> getUnreadNotifications();
}
