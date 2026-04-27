package skytrack.business.useCase.booking;

import skytrack.dto.booking.BookingResponse;
import skytrack.dto.booking.CreateBookingRequest;

public interface CreateBookingUseCase {
    BookingResponse toResponse(CreateBookingRequest request);
}
