package skytrack.dto.duffel.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Builder
public class RequestData {
    private List<RequestSlice> slices;
    private List<DuffelPassenger> passengers;
    @JsonProperty("cabin_class")
    private String cabinClass;

    @JsonProperty("max_connection")
    private Integer maxConnections;
}
