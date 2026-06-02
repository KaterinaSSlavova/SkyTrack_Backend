package skytrack.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.booking.BookingNotFoundException;
import skytrack.business.impl.booking.GetBookingUseCaseImpl;
import skytrack.business.mapper.BookingMapper;
import skytrack.dto.booking.BookingResponse;
import skytrack.persistence.entity.BookingEntity;
import skytrack.persistence.repo.BookingRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBookingUseCaseImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private GetBookingUseCaseImpl getBookingUseCase;

    @Test
    void getBooking_shouldReturnMappedResponse() {
        BookingEntity entity = new BookingEntity();
        BookingResponse response = new BookingResponse();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(bookingMapper.toResponse(entity)).thenReturn(response);

        BookingResponse result = getBookingUseCase.getBooking(1L);

        assertThat(result).isEqualTo(response);
        verify(bookingMapper).toResponse(entity);
    }

    @Test
    void getBooking_shouldThrowBookingNotFoundException_whenBookingDoesNotExist() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> getBookingUseCase.getBooking(99L));

        verify(bookingMapper, never()).toResponse(any());
    }
}
