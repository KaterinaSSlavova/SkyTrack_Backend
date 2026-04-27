package skytrack.business.useCase.booking;

import skytrack.dto.booking.BookingResponse;

public interface GetBookingUseCase {
    BookingResponse getBooking(Long id);
}
