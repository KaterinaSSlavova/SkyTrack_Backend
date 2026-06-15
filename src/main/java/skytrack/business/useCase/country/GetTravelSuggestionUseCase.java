package skytrack.business.useCase.country;

import skytrack.dto.country.VisitedCountryResponse;

import java.util.List;

public interface GetTravelSuggestionUseCase {
    List<VisitedCountryResponse> getSuggestion();
}
