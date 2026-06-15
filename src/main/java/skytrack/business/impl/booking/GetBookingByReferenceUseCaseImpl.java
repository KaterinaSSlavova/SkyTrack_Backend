package skytrack.business.impl.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.user.AccessDeniedException;
import skytrack.business.mapper.BookingMapper;
import skytrack.business.service.UserService;
import skytrack.business.useCase.booking.GetBookingByReferenceUseCase;
import skytrack.dto.booking.BookingResponse;
import skytrack.persistence.entity.BookingEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.repo.BookingRepository;
import skytrack.persistence.repo.UserRepository;

@Service
@RequiredArgsConstructor
public class GetBookingByReferenceUseCaseImpl implements GetBookingByReferenceUseCase {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;

    @Override
    public BookingResponse getBookingByReference(String reference) {
        UserEntity user = userService.getLoggedUser();
        BookingEntity booking = bookingRepository.getByBookingReference(reference);

        if(!booking.getUser().getEmail().equals(user.getEmail())){
            throw new AccessDeniedException();
        }

        return bookingMapper.toResponse(booking);
    }
}
