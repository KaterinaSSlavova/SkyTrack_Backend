package skytrack.dto.airport;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAirportRequest {

    @NotBlank(message = "IATA code is required.")
    private String iataCode;

    @NotBlank(message = "Airport name is required.")
    private String name;

    @NotBlank(message = "City is required.")
    private String city;

    @NotBlank(message = "Country is required.")
    private String country;

    @NotBlank(message = "Timezone is required.")
    private String timezone;
}