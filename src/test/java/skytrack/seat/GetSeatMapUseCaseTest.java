package skytrack.seat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.flight.FlightNotFoundException;
import skytrack.business.impl.seat.GetSeatMapUseCaseImpl;
import skytrack.business.mapper.SeatMapper;
import skytrack.dto.seat.SeatMapResponse;
import skytrack.dto.seat.SeatResponse;
import skytrack.persistence.entity.BookingEntity;
import skytrack.persistence.entity.SeatEntity;
import skytrack.persistence.repo.BookingRepository;
import skytrack.persistence.repo.DuffelRepository;
import skytrack.persistence.repo.SeatRepository;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GetSeatMapUseCaseTest {
    @Mock
    private SeatRepository seatRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private DuffelRepository duffelRepository;

    @Mock
    private SeatMapper seatMapper;

    @InjectMocks
    private GetSeatMapUseCaseImpl getSeatMapUseCase;

    @Test
    void getSeatMap_shouldReturnSeatMapWithCorrectAvailability_whenFlightExists(){
        //arrange
        List<SeatEntity> seats = new ArrayList<>() {{ add(new SeatEntity(1L, "1A", true, false, true));
            add(new SeatEntity(2L, "1B", false, false, true));}};
        SeatResponse response1 = SeatResponse.builder().id(1L).available(false).build();
        SeatResponse response2 = SeatResponse.builder().id(2L).available(true).build();
        BookingEntity booking = new BookingEntity();
        booking.setSeat(seats.get(0));
        when(duffelRepository.existsById(1L)).thenReturn(true);
        when(bookingRepository.findByExternalFlight_Id(1L)).thenReturn(List.of(booking));
        when(seatRepository.findAll()).thenReturn(seats);
        when(seatMapper.toResponse(seats.get(0), false)).thenReturn(response1);
        when(seatMapper.toResponse(seats.get(1), true)).thenReturn(response2);

        //act
        SeatMapResponse result = getSeatMapUseCase.getSeatMap(1L);

        //assert
        assertThat(result.getFlight_id()).isEqualTo(1L);
        assertThat(result.getSeats()).hasSize(2);
        assertThat(result.getSeats().get(0).getAvailable()).isFalse();
        assertThat(result.getSeats().get(1).getAvailable()).isTrue();
    }

    @Test
    void getSeatMap_shouldThrowFlightNotFoundException_whenDoesNotExist(){
        //arrange
        when(duffelRepository.existsById(1L)).thenReturn(false);

        //act and assert
        assertThrows(FlightNotFoundException.class, () -> getSeatMapUseCase.getSeatMap(1L));
    }
}
