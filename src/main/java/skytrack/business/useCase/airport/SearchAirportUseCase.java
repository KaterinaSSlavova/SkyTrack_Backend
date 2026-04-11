package skytrack.business.useCase.airport;

import skytrack.dto.airport.AirportResponse;

import java.util.List;

public interface SearchAirportUseCase {
    List<AirportResponse> searchAirport(String input);
}
