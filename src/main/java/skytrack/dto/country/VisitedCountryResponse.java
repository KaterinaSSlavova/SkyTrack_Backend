package skytrack.dto.country;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisitedCountryResponse {
    private String countryCode;
    private String countryName;
}
