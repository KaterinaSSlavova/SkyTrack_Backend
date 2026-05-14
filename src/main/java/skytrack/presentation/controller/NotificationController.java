package skytrack.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import skytrack.business.useCase.notification.*;
import skytrack.dto.notification.NotificationResponse;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final GetNotificationUseCase getNotificationUseCase;
    private final GetAllNotificationsUseCase getAllNotificationsUseCase;
    private final MarkAllNotificationsAsReadUseCase markAllNotificationsAsReadUseCase;
    private final MarkNotificationAsReadUseCase markNotificationAsReadUseCase;
    private final GetUnreadNotificationsUseCase getUnreadNotificationsUseCase;

    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        List<NotificationResponse> response = getAllNotificationsUseCase.getAllNotifications();
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications() {
        List<NotificationResponse> response = getUnreadNotificationsUseCase.getUnreadNotifications();
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotification(@PathVariable("id")final Long id) {
        NotificationResponse response = getNotificationUseCase.getNotification(id);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable("id")final Long id) {
        markNotificationAsReadUseCase.markNotificationAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {
        markAllNotificationsAsReadUseCase.markAllNotificationsAsRead();
        return ResponseEntity.noContent().build();
    }
}
