package skytrack.business.useCase.airport;

import skytrack.dto.airport.AirportResponse;

public interface GetAirportUseCase {
    AirportResponse getAirportById(Long id);
}
