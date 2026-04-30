package skytrack.business.useCase.flight;

import skytrack.dto.duffel.DuffelFlightResponse;
import skytrack.dto.duffel.SavedFlightResponse;

public interface SaveDuffelUseCase {
    SavedFlightResponse saveFlight(DuffelFlightResponse response);
}
