package skytrack.business.useCase.flight;

import skytrack.dto.flight.CreateFlightRequest;
import skytrack.dto.flight.FlightResponse;

public interface CreateFlightUseCase {
    FlightResponse createFlight(CreateFlightRequest request);
}
