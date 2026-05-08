package skytrack.business.impl.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.mapper.BookingMapper;
import skytrack.business.useCase.booking.GetBookingByReferenceUseCase;
import skytrack.dto.booking.BookingResponse;
import skytrack.persistence.repo.BookingRepository;

@Service
@RequiredArgsConstructor
public class GetBookingByReferenceUseCaseImpl implements GetBookingByReferenceUseCase {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponse getBookingByReference(String reference) {
        return bookingMapper.toResponse(bookingRepository.getByBookingReference(reference));
    }
}
