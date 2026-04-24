package skytrack.business.useCase.flight;

import reactor.core.publisher.Mono;
import skytrack.dto.duffel.DuffelFlightResponse;

import java.time.LocalDate;
import java.util.List;

public interface SearchDuffelFlightUseCase {
    Mono<List<DuffelFlightResponse>> searchFlights
            (String departureIata, String arrivalIata, LocalDate departureDate);
}
