package skytrack.seat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.flight.FlightNotFoundException;
import skytrack.business.exception.seat.SeatNotFoundException;
import skytrack.business.impl.seat.GetSeatUseCaseImpl;
import skytrack.business.mapper.SeatMapper;
import skytrack.dto.seat.SeatResponse;
import skytrack.persistence.entity.SeatEntity;
import skytrack.persistence.repo.BookingRepository;
import skytrack.persistence.repo.DuffelRepository;
import skytrack.persistence.repo.SeatRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetSeatUseCaseTest {
    @Mock
    private SeatRepository seatRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private DuffelRepository duffelRepository;

    @Mock
    private SeatMapper seatMapper;

    @InjectMocks
    private GetSeatUseCaseImpl getSeat;

    @Test
    void getSeat_shouldThrowSeatNotFoundException_whenSeatDoesNotExist() {
        // arrange
        when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        // act and assert
        assertThrows(SeatNotFoundException.class, () -> getSeat.getSeat(1L, 1L));
    }

    @Test
    void getSeat_shouldThrowFlightNotFoundException_whenFlightDoesNotExist() {
        // arrange
        when(seatRepository.findById(1L)).thenReturn(Optional.of(new SeatEntity()));
        when(duffelRepository.existsById(1L)).thenReturn(false);

        // act and assert
        assertThrows(FlightNotFoundException.class, () -> getSeat.getSeat(1L, 1L));
    }

    @Test
    void getSeat_shouldReturnSeatWithAvailability_whenSeatAndFlightExist() {
        // arrange
        SeatEntity seat = new SeatEntity();
        SeatResponse expected = new SeatResponse();

        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(duffelRepository.existsById(1L)).thenReturn(true);
        when(bookingRepository.existsByExternalFlight_IdAndSeat_Id(1L, 1L)).thenReturn(false);
        when(seatMapper.toResponse(seat, true)).thenReturn(expected);

        // act
        SeatResponse result = getSeat.getSeat(1L, 1L);

        // assert
        assertNotNull(result);
        verify(seatMapper).toResponse(seat, true);
    }
}
