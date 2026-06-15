package skytrack.dto.country;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TravelMapResponse {
    private List<VisitedCountryResponse> visitedCountries;
    private List<VisitedCountryResponse> suggestedCountries;
}
