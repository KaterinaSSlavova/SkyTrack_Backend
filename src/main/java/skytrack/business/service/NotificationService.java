package skytrack.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import skytrack.business.mapper.NotificationMapper;
import skytrack.dto.notification.NotificationResponse;
import skytrack.persistence.entity.DuffelFlightEntity;
import skytrack.persistence.entity.NotificationEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.enumeration.NotificationType;
import skytrack.persistence.repo.NotificationRepository;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMessageService notificationMessageService;
    private final TimeConverter timeConverter;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationMapper mapper;

    public void createNotification(NotificationType type, UserEntity user, DuffelFlightEntity flight) {
        String title = notificationMessageService.getTitle(type);
        LocalDateTime depTime = timeConverter.convertToLocalTime
                (flight.getDepartureTime(), flight.getDepartureTimezone());
        String message = notificationMessageService.getMessage
                (type, flight.getFlightNumber(), flight.getGate(), depTime);

        NotificationEntity notification = NotificationEntity.builder()
                .user(user)
                .flight(flight)
                .type(type)
                .title(title)
                .message(message)
                .read(false)
                .createdAt(Instant.now())
                .build();

        NotificationResponse response = mapper.toResponse(notificationRepository.save(notification));
        messagingTemplate.convertAndSendToUser(user.getEmail(), "/queue/notifications", response);
    }
}