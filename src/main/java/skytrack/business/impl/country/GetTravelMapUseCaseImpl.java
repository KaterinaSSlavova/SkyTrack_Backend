package skytrack.business.impl.country;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.service.TravelSuggestionService;
import skytrack.business.service.UserService;
import skytrack.business.useCase.country.GetTravelMapUseCase;
import skytrack.dto.country.TravelMapResponse;
import skytrack.dto.country.VisitedCountryResponse;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.entity.VisitedCountryEntity;
import skytrack.persistence.repo.VisitedCountryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTravelMapUseCaseImpl implements GetTravelMapUseCase {
    private final UserService userService;
    private final VisitedCountryRepository visitedCountryRepository;
    private final TravelSuggestionService travelSuggestionService;

    @Override
    public TravelMapResponse getTravelMap() {
        UserEntity user = userService.getLoggedUser();
        List<VisitedCountryEntity> visited =  visitedCountryRepository.findByUserId(user.getId());
        List<VisitedCountryResponse> visitedResponse = visited.stream()
                .map(c -> VisitedCountryResponse.builder()
                        .countryCode(c.getCountryCode()).countryName(c.getCountryName()).build()).toList();

        return TravelMapResponse.builder()
                .visitedCountries(visitedResponse)
                .suggestedCountries(travelSuggestionService.getSuggestions())
                .build();
    }
}
