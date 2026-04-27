package skytrack.business.useCase.booking;

import skytrack.dto.booking.BookingResponse;

import java.util.List;

public interface GetAllBookingsUseCase {
    List<BookingResponse> getAllBookings();
}
