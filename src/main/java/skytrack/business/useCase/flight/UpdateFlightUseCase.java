package skytrack.business.useCase.flight;

import skytrack.dto.flight.UpdateFlightRequest;

public interface UpdateFlightUseCase {
    void updateFlight(UpdateFlightRequest request);
}
