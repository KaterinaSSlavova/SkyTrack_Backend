package skytrack.business.useCase.flight;

import skytrack.dto.duffel.SavedFlightResponse;

public interface GetDuffelFlightUseCase {
    SavedFlightResponse getDuffelFlightById(Long id);
}
