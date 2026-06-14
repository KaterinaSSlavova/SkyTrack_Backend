package skytrack.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.booking.BookingNotFoundException;
import skytrack.business.impl.booking.GetBookingUseCaseImpl;
import skytrack.business.mapper.BookingMapper;
import skytrack.business.service.UserService;
import skytrack.dto.booking.BookingResponse;
import skytrack.persistence.entity.BookingEntity;
import skytrack.persistence.entity.UserEntity;
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

    @Mock
    private UserService userService;

    @InjectMocks
    private GetBookingUseCaseImpl getBookingUseCase;

    @Test
    void getBooking_shouldReturnMappedResponse() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");

        BookingEntity entity = new BookingEntity();
        entity.setUser(user);

        BookingResponse response = new BookingResponse();

        when(userService.getLoggedUser()).thenReturn(user);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(bookingMapper.toResponse(entity)).thenReturn(response);

        BookingResponse result = getBookingUseCase.getBooking(1L);

        assertThat(result).isEqualTo(response);
        verify(bookingMapper).toResponse(entity);
    }

    @Test
    void getBooking_shouldThrowBookingNotFoundException_whenBookingDoesNotExist() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");

        when(userService.getLoggedUser()).thenReturn(user);
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> getBookingUseCase.getBooking(99L));

        verify(bookingMapper, never()).toResponse(any());
    }
}
