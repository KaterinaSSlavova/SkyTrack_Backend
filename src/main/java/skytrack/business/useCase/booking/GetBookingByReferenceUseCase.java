package skytrack.business.useCase.booking;

import skytrack.dto.booking.BookingResponse;

public interface GetBookingByReferenceUseCase {
    BookingResponse getBookingByReference(String reference);
}
