package skytrack.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import skytrack.persistence.enumeration.NotificationType;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="notification")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id")
    private DuffelFlightEntity flight;

    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name="is_read")
    private boolean read;

    @Column(name="created_at")
    private Instant createdAt;
}
