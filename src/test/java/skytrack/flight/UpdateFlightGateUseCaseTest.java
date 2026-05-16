package skytrack.flight;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.flight.FlightNotFoundException;
import skytrack.business.impl.duffel.UpdateFlightGateUseCaseImpl;
import skytrack.business.service.NotificationService;
import skytrack.persistence.entity.BookingEntity;
import skytrack.persistence.entity.DuffelFlightEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.enumeration.NotificationType;
import skytrack.persistence.repo.BookingRepository;
import skytrack.persistence.repo.DuffelRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateFlightGateUseCaseTest {
    @Mock
    private DuffelRepository duffelRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private UpdateFlightGateUseCaseImpl updateFlightGate;

    @Test
    void updateFlightGate_shouldThrowFlightNotFoundException_whenFlightDoesNotExist() {
        // arrange
        when(duffelRepository.findById(1L)).thenReturn(Optional.empty());

        // act and assert
        assertThrows(FlightNotFoundException.class, () -> updateFlightGate.updateFlightGate(1L, "A12"));
    }

    @Test
    void updateFlightGate_shouldUpdateGateAndNotifyPassengers_whenFlightExists() {
        // arrange
        DuffelFlightEntity flight = new DuffelFlightEntity();
        UserEntity user = new UserEntity();
        BookingEntity booking = new BookingEntity();
        booking.setUser(user);

        when(duffelRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(bookingRepository.findByExternalFlight_Id(any())).thenReturn(List.of(booking));

        // act
        updateFlightGate.updateFlightGate(1L, "A12");

        // assert
        verify(duffelRepository).save(flight);
        verify(notificationService).createNotification(NotificationType.GATE_CHANGED, user, flight);
    }

    @Test
    void updateFlightGate_shouldNotNotify_whenNoBookingsExist() {
        // arrange
        DuffelFlightEntity flight = new DuffelFlightEntity();

        when(duffelRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(bookingRepository.findByExternalFlight_Id(any())).thenReturn(List.of());

        // act
        updateFlightGate.updateFlightGate(1L, "A12");

        // assert
        verify(duffelRepository).save(flight);
        verify(notificationService, never()).createNotification(any(), any(), any());
    }
}
