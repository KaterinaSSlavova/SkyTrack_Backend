package skytrack.dto.flight;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponse {
    private Long id;
    private String flightNumber;
    private String departureIATACode;
    private String ArrivalIATACode;
    private LocalDateTime departureLocalTime;
    private LocalDateTime arrivalLocalTime;
    private String departureTimezone;
    private String arrivalTimezone;
    private String gate;
    private String terminal;
    private BigDecimal price;
    private Integer capacity;
    private String status;
}
