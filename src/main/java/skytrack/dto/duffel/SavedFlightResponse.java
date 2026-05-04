package skytrack.dto.duffel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import skytrack.dto.airport.AirportResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SavedFlightResponse {
    private Long id;
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
    private AirportResponse departureAirport;
    private AirportResponse arrivalAirport;
}