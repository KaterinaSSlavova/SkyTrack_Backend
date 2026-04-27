package skytrack.business.impl.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.mapper.BookingMapper;
import skytrack.business.useCase.booking.GetAllBookingsUseCase;
import skytrack.dto.booking.BookingResponse;
import skytrack.persistence.repo.BookingRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllBookingsUseCaseImpl implements GetAllBookingsUseCase {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream().map(bookingMapper::toResponse).toList();
    }
}
