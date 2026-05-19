package skytrack.flight;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.flight.FlightNotFoundException;
import skytrack.business.impl.duffel.UpdateFlightStatusUseCaseImpl;
import skytrack.business.service.NotificationService;
import skytrack.business.service.TimeConverter;
import skytrack.persistence.entity.BookingEntity;
import skytrack.persistence.entity.DuffelFlightEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.enumeration.FlightStatus;
import skytrack.persistence.enumeration.NotificationType;
import skytrack.persistence.repo.BookingRepository;
import skytrack.persistence.repo.DuffelRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateFlightStatusUseCaseTest {
    @Mock
    private DuffelRepository duffelRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TimeConverter timeConverter;

    @InjectMocks
    private UpdateFlightStatusUseCaseImpl updateFlightStatus;

    @Test
    void updateFlightStatus_shouldThrowFlightNotFoundException_whenFlightDoesNotExist() {
        // arrange
        when(duffelRepository.findById(1L)).thenReturn(Optional.empty());

        // act and assert
        assertThrows(FlightNotFoundException.class,
                () -> updateFlightStatus.updateFlightStatus(1L, FlightStatus.DELAYED, LocalDateTime.now()));
    }

    @Test
    void updateFlightStatus_shouldUpdateDepartureTime_whenStatusIsDelayed() {
        // arrange
        DuffelFlightEntity flight = new DuffelFlightEntity();
        flight.setDepartureTimezone("Europe/Amsterdam");
        UserEntity user = new UserEntity();
        BookingEntity booking = new BookingEntity();
        booking.setUser(user);
        Instant newTime = Instant.now();

        when(duffelRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(timeConverter.convertToUTC(any(), any())).thenReturn(newTime);
        when(bookingRepository.findByExternalFlight_Id(any())).thenReturn(List.of(booking));

        // act
        updateFlightStatus.updateFlightStatus(1L, FlightStatus.DELAYED, LocalDateTime.now());

        // assert
        verify(timeConverter).convertToUTC(any(), any());
        verify(duffelRepository).save(flight);
        verify(notificationService).createNotification(NotificationType.FLIGHT_DELAYED, user, flight);
    }

    @Test
    void updateFlightStatus_shouldArchiveBookingsAndNotify_whenStatusIsCancelled() {
        // arrange
        DuffelFlightEntity flight = new DuffelFlightEntity();
        UserEntity user = new UserEntity();
        BookingEntity booking = new BookingEntity();
        booking.setUser(user);
        booking.setArchived(false);

        when(duffelRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(bookingRepository.findByExternalFlight_Id(any())).thenReturn(List.of(booking));

        // act
        updateFlightStatus.updateFlightStatus(1L, FlightStatus.CANCELLED, null);

        // assert
        assertTrue(booking.isArchived());
        verify(bookingRepository).save(booking);
        verify(notificationService).createNotification(NotificationType.FLIGHT_CANCELLED, user, flight);
    }

    @Test
    void updateFlightStatus_shouldNotNotify_whenStatusIsScheduled() {
        // arrange
        DuffelFlightEntity flight = new DuffelFlightEntity();

        when(duffelRepository.findById(1L)).thenReturn(Optional.of(flight));

        // act
        updateFlightStatus.updateFlightStatus(1L, FlightStatus.SCHEDULED, null);

        // assert
        verify(duffelRepository).save(flight);
        verify(notificationService, never()).createNotification(any(), any(), any());
        verify(bookingRepository, never()).findByExternalFlight_Id(any());
    }

    @Test
    void updateFlightStatus_shouldNotNotify_whenNoBookingsExist() {
        // arrange
        DuffelFlightEntity flight = new DuffelFlightEntity();
        flight.setDepartureTimezone("Europe/Amsterdam");

        when(duffelRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(timeConverter.convertToUTC(any(), any())).thenReturn(Instant.now());
        when(bookingRepository.findByExternalFlight_Id(any())).thenReturn(List.of());

        // act
        updateFlightStatus.updateFlightStatus(1L, FlightStatus.DELAYED, LocalDateTime.now());

        // assert
        verify(duffelRepository).save(flight);
        verify(notificationService, never()).createNotification(any(), any(), any());
    }
}
