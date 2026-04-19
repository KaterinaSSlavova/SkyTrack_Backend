package skytrack.dto.flight;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateFlightRequest {

    @NotBlank(message = "Flight number is required.")
    private String flightNumber;

    @NotNull(message = "Departure airport is required.")
    private Long departureAirportId;

    @NotNull(message = "Arrival airport is required.")
    private Long arrivalAirportId;

    @NotNull(message = "Departure time is required.")
    private LocalDateTime departureLocalTime;

    @NotNull(message = "Arrival time is required.")
    private LocalDateTime arrivalLocalTime;

    private String gate;

    private String terminal;

    @NotNull(message = "Capacity is required.")
    @Positive(message = "Capacity must be greater than 0.")
    private Integer capacity;

    @NotNull(message = "Price is required.")
    @Positive(message = "Price must be greater than 0.")
    private BigDecimal price;

    @NotNull(message = "Flight status is required.")
    private Long statusId;
}