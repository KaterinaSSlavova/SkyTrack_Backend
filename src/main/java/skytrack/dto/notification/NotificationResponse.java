package skytrack.dto.notification;

import lombok.*;
import skytrack.persistence.enumeration.NotificationType;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private Long flightId;
    private String flightNumber;
    private NotificationType type;
    private String title;
    private String message;
    private boolean read;
    private Instant createdAt;
}
