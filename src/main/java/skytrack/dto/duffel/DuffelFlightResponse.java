package skytrack.dto.duffel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DuffelFlightResponse {
    private String externalId;
    private String flightNumber;
    private String departureIataCode;
    private String arrivalIataCode;
    private LocalDateTime departureLocalTime;
    private LocalDateTime arrivalLocalTime;
    private String departureTimezone;
    private String arrivalTimezone;
    private BigDecimal price;
    private String currency;
}
