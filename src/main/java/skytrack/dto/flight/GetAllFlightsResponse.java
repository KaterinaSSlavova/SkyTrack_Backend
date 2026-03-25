package skytrack.dto.flight;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetAllFlightsResponse {
    private List<FlightResponse> flights;
}
