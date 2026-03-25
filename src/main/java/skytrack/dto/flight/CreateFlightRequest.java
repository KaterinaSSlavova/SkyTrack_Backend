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
    @NotBlank
    private String flightNumber;

    @NotNull
    private Long departureAirportId;

    @NotNull
    private Long arrivalAirportId;

    @NotNull
    private LocalDateTime departureLocalTime;

    @NotNull
    private LocalDateTime arrivalLocalTime;

    private String gate;
    private String terminal;

    @NotNull
    @Positive
    private Integer capacity;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotNull
    private Long statusId;
}
