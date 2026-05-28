package skytrack.business.impl.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.mapper.BookingMapper;
import skytrack.business.service.UserService;
import skytrack.business.useCase.booking.GetAllBookingsUseCase;
import skytrack.dto.booking.BookingResponse;
import skytrack.persistence.repo.BookingRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllBookingsUseCaseImpl implements GetAllBookingsUseCase {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;

    @Override
    public List<BookingResponse> getAllBookings() {
        Long id = userService.getLoggedUser().getId();
        return bookingRepository.findByUser_Id(id).stream().map(bookingMapper::toResponse).toList();
    }
}
