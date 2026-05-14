package skytrack.business.useCase.flight;

import skytrack.dto.duffel.SavedFlightResponse;

import java.util.List;

public interface GetAllDuffelFlightsUseCase {
    List<SavedFlightResponse> getAllDuffelFlights();
}
