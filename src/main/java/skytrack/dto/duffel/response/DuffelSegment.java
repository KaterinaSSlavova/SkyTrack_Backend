package skytrack.dto.duffel.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DuffelSegment {
    private String departingAt;
    private String arrivingAt;
    private String operatingCarrierFlightNumber;
    private DuffelAirport origin;
    private DuffelAirport destination;
}
