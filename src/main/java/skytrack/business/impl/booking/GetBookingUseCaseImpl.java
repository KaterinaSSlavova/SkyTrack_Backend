package skytrack.business.impl.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.booking.BookingNotFoundException;
import skytrack.business.mapper.BookingMapper;
import skytrack.business.useCase.booking.GetBookingUseCase;
import skytrack.dto.booking.BookingResponse;
import skytrack.persistence.repo.BookingRepository;

@Service
@RequiredArgsConstructor
public class GetBookingUseCaseImpl implements GetBookingUseCase {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponse getBooking(Long id) {
        return bookingMapper.toResponse(bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id)));
    }
}
