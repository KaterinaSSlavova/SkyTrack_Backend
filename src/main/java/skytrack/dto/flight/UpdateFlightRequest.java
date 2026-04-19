package skytrack.dto.flight;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFlightRequest {
    private Long id;

    @NotBlank(message = "Flight number is required.")
    private String flightNumber;
    private Long departureAirportId;
    private Long arrivalAirportId;
    private LocalDateTime departureLocalTime;
    private LocalDateTime arrivalLocalTime;

    private String gate;

    private String terminal;

    @NotNull(message = "Capacity is required.")
    @Positive(message = "Capacity must be greater than 0.")
    private Integer capacity;

    @NotNull(message = "Price is required.")
    @Positive(message = "Price must be greater than 0.")
    private BigDecimal price;
    private Long statusId;
}
