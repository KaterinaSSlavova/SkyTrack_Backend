package skytrack.business.useCase.statistics;

import skytrack.dto.booking.BookingResponse;

public interface GetNextFlightUseCase {
    BookingResponse getNextFlight();
}
