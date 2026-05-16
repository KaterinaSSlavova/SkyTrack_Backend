package skytrack.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.booking.BookingNotFoundException;
import skytrack.business.impl.booking.CancelBookingUseCaseImpl;
import skytrack.persistence.entity.BookingEntity;
import skytrack.persistence.repo.BookingRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CancelBookingUseCaseTest {
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private CancelBookingUseCaseImpl cancelBooking;

    @Test
    void cancelBooking_shouldThrowBookingNotFoundException_whenBookingDoesNotExist() {
        // arrange
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        // act and assert
        assertThrows(BookingNotFoundException.class, () -> cancelBooking.cancelBooking(1L));
    }

    @Test
    void cancelBooking_shouldSetArchivedTrue_whenBookingExists() {
        // arrange
        BookingEntity booking = new BookingEntity();
        booking.setArchived(false);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // act
        cancelBooking.cancelBooking(1L);

        // assert
        assertTrue(booking.isArchived());
        verify(bookingRepository).save(booking);
    }
}
