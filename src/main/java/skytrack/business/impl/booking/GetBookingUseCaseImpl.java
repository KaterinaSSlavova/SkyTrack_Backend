package skytrack.business.impl.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.booking.BookingNotFoundException;
import skytrack.business.exception.user.AccessDeniedException;
import skytrack.business.mapper.BookingMapper;
import skytrack.business.service.UserService;
import skytrack.business.useCase.booking.GetBookingUseCase;
import skytrack.dto.booking.BookingResponse;
import skytrack.persistence.entity.BookingEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.repo.BookingRepository;

@Service
@RequiredArgsConstructor
public class GetBookingUseCaseImpl implements GetBookingUseCase {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;

    @Override
    public BookingResponse getBooking(Long id) {
       UserEntity user = userService.getLoggedUser();
       BookingEntity booking = bookingRepository.findById(id)
               .orElseThrow(() -> new BookingNotFoundException(id));
       if(!booking.getUser().getEmail().equals(user.getEmail())){
           throw new AccessDeniedException();
       }
        return bookingMapper.toResponse(booking);
    }
}
