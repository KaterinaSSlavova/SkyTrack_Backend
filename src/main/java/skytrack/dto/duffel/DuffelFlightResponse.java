package skytrack.dto.duffel;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class DuffelFlightResponse {
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
