package skytrack.business.impl.country;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.service.TravelSuggestionService;
import skytrack.business.useCase.country.GetTravelSuggestionUseCase;
import skytrack.dto.country.VisitedCountryResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTravelSuggestionUseCaseImpl implements GetTravelSuggestionUseCase {
    private final TravelSuggestionService travelSuggestionService;

    @Override
    public List<VisitedCountryResponse> getSuggestion() {
        return travelSuggestionService.getSuggestions();
    }
}
