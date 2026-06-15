package skytrack.statistics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.impl.statistics.GetNextFlightUseCaseImpl;
import skytrack.business.mapper.BookingMapper;
import skytrack.business.service.UserService;
import skytrack.dto.booking.BookingResponse;
import skytrack.persistence.entity.BookingEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.repo.BookingRepository;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetNextFlightUseCaseImplTest {
    @Mock
    private UserService userService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private GetNextFlightUseCaseImpl getNextFlightUseCase;

    @Test
    void getNextFlight_shouldReturnBookingResponse_whenUpcomingBookingExists() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .build();

        BookingEntity booking = new BookingEntity();
        BookingResponse response = new BookingResponse();

        when(userService.getLoggedUser()).thenReturn(user);
        when(bookingRepository.findNextUpcoming(eq(1L), any(Instant.class)))
                .thenReturn(Optional.of(booking));
        when(bookingMapper.toResponse(booking)).thenReturn(response);

        BookingResponse result = getNextFlightUseCase.getNextFlight();

        assertEquals(response, result);

        verify(userService).getLoggedUser();
        verify(bookingRepository).findNextUpcoming(eq(1L), any(Instant.class));
        verify(bookingMapper).toResponse(booking);
    }

    @Test
    void getNextFlight_shouldReturnNull_whenNoUpcomingBookingExists() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .build();

        when(userService.getLoggedUser()).thenReturn(user);
        when(bookingRepository.findNextUpcoming(eq(1L), any(Instant.class)))
                .thenReturn(Optional.empty());

        BookingResponse result = getNextFlightUseCase.getNextFlight();

        assertNull(result);

        verify(userService).getLoggedUser();
        verify(bookingRepository).findNextUpcoming(eq(1L), any(Instant.class));
        verify(bookingMapper, never()).toResponse(any());
    }
}
