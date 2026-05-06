package skytrack.business.impl.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.booking.BookingNotFoundException;
import skytrack.business.useCase.booking.CancelBookingUseCase;
import skytrack.persistence.entity.BookingEntity;
import skytrack.persistence.repo.BookingRepository;

@Service
@RequiredArgsConstructor
public class CancelBookingUseCaseImpl implements CancelBookingUseCase {
    private final BookingRepository bookingRepository;

    @Override
    public void cancelBooking(Long id) {
        BookingEntity entity = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
        entity.setArchived(true);
        bookingRepository.save(entity);
    }
}
