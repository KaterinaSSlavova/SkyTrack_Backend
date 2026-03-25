package skytrack.business.useCase.flight;

import skytrack.dto.flight.FlightResponse;

import java.util.Optional;

public interface GetFlightUseCase {
    FlightResponse getFlightById(Long flightId);
}
