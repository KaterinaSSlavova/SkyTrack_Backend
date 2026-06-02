package skytrack.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import skytrack.business.mapper.NotificationMapper;
import skytrack.business.service.NotificationMessageService;
import skytrack.business.service.NotificationService;
import skytrack.business.service.TimeConverter;
import skytrack.dto.notification.NotificationResponse;
import skytrack.persistence.entity.DuffelFlightEntity;
import skytrack.persistence.entity.NotificationEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.enumeration.NotificationType;
import skytrack.persistence.repo.NotificationRepository;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMessageService notificationMessageService;

    @Mock
    private TimeConverter timeConverter;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private NotificationMapper mapper;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void createNotification_shouldSaveAndSendNotification() {
        UserEntity user = new UserEntity();
        user.setEmail("jane@example.com");

        DuffelFlightEntity flight = new DuffelFlightEntity();
        flight.setFlightNumber("SK123");
        flight.setGate("B12");
        flight.setDepartureTime(Instant.now());
        flight.setDepartureTimezone("Europe/Amsterdam");

        NotificationEntity savedEntity = new NotificationEntity();
        NotificationResponse response = NotificationResponse.builder().id(1L).build();

        when(notificationMessageService.getTitle(NotificationType.GATE_CHANGED)).thenReturn("Gate Changed");
        when(timeConverter.convertToLocalTime(any(), any())).thenReturn(LocalDateTime.now());
        when(notificationMessageService.getMessage(any(), any(), any(), any())).thenReturn("Gate changed to B12");
        when(notificationRepository.save(any(NotificationEntity.class))).thenReturn(savedEntity);
        when(mapper.toResponse(savedEntity)).thenReturn(response);

        notificationService.createNotification(NotificationType.GATE_CHANGED, user, flight);

        ArgumentCaptor<NotificationEntity> captor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(notificationRepository).save(captor.capture());

        NotificationEntity saved = captor.getValue();
        assertThat(saved.getUser()).isEqualTo(user);
        assertThat(saved.getFlight()).isEqualTo(flight);
        assertThat(saved.getType()).isEqualTo(NotificationType.GATE_CHANGED);
        assertThat(saved.getTitle()).isEqualTo("Gate Changed");
        assertThat(saved.getMessage()).isEqualTo("Gate changed to B12");
        assertThat(saved.isRead()).isFalse();
        assertThat(saved.getCreatedAt()).isNotNull();

        verify(messagingTemplate).convertAndSendToUser(
                eq("jane@example.com"),
                eq("/queue/notifications"),
                eq(response)
        );
    }

    @Test
    void createNotification_shouldUseCorrectNotificationType() {
        UserEntity user = new UserEntity();
        user.setEmail("jane@example.com");

        DuffelFlightEntity flight = new DuffelFlightEntity();
        flight.setFlightNumber("SK123");
        flight.setDepartureTime(Instant.now());
        flight.setDepartureTimezone("Europe/Amsterdam");

        when(notificationMessageService.getTitle(NotificationType.FLIGHT_DELAYED)).thenReturn("Flight Delayed");
        when(timeConverter.convertToLocalTime(any(), any())).thenReturn(LocalDateTime.now());
        when(notificationMessageService.getMessage(any(), any(), any(), any())).thenReturn("Your flight is delayed");
        when(notificationRepository.save(any())).thenReturn(new NotificationEntity());
        when(mapper.toResponse(any())).thenReturn(NotificationResponse.builder().id(2L).build());

        notificationService.createNotification(NotificationType.FLIGHT_DELAYED, user, flight);

        verify(notificationMessageService).getTitle(NotificationType.FLIGHT_DELAYED);
        verify(notificationMessageService).getMessage(eq(NotificationType.FLIGHT_DELAYED), any(), any(), any());
    }
}
