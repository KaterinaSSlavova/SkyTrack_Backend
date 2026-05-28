package skytrack.business.impl.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.mapper.BookingMapper;
import skytrack.business.service.UserService;
import skytrack.business.useCase.statistics.GetNextFlightUseCase;
import skytrack.dto.booking.BookingResponse;
import skytrack.persistence.entity.BookingEntity;
import skytrack.persistence.repo.BookingRepository;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetNextFlightUseCaseImpl implements GetNextFlightUseCase {
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponse getNextFlight() {
        Long id = userService.getLoggedUser().getId();
        Optional<BookingEntity> booking = bookingRepository.findNextUpcoming(id, Instant.now());
        return booking.map(bookingMapper::toResponse).orElse(null);
    }
}
