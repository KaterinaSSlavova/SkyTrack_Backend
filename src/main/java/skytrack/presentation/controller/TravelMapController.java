package skytrack.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import skytrack.business.useCase.country.GetTravelMapUseCase;
import skytrack.business.useCase.country.GetTravelSuggestionUseCase;
import skytrack.business.useCase.country.MarkCountryAsVisitedUseCase;
import skytrack.business.useCase.country.RemoveVisitedCountryUseCase;
import skytrack.dto.country.TravelMapResponse;
import skytrack.dto.country.VisitedCountryResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class TravelMapController {
    private final RemoveVisitedCountryUseCase removeVisitedCountryUseCase;
    private final MarkCountryAsVisitedUseCase markCountryAsVisitedUseCase;
    private final GetTravelMapUseCase getTravelMapUseCase;
    private final GetTravelSuggestionUseCase getTravelSuggestionUseCase;

    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping
    public ResponseEntity<TravelMapResponse> getTravelMap() {
        TravelMapResponse response = getTravelMapUseCase.getTravelMap();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping("/suggestions")
    public ResponseEntity<List<VisitedCountryResponse>> getSuggestions() {
        List<VisitedCountryResponse> response = getTravelSuggestionUseCase.getSuggestion();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping("/{countryCode}")
    public ResponseEntity<Void> markCountryAsVisited
            (@PathVariable("countryCode") final String countryCode) {
        markCountryAsVisitedUseCase.markCountry(countryCode);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @DeleteMapping("/{countryCode}")
    public ResponseEntity<Void> removeVisitedCountry
            (@PathVariable("countryCode") final String countryCode) {
        removeVisitedCountryUseCase.removeCountry(countryCode);
        return ResponseEntity.noContent().build();
    }
}
