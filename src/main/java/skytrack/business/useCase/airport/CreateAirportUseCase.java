package skytrack.business.useCase.airport;

import skytrack.dto.airport.AirportResponse;
import skytrack.dto.airport.CreateAirportRequest;

public interface CreateAirportUseCase {
    AirportResponse createAirport(CreateAirportRequest request);
}
