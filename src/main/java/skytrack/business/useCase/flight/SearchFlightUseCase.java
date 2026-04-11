package skytrack.business.useCase.flight;

import skytrack.dto.flight.FlightResponse;

import java.time.LocalDate;
import java.util.List;

public interface SearchFlightUseCase {
    List<FlightResponse> searchFlights(String departureIata, String arrivalIata, LocalDate departureDate);
}