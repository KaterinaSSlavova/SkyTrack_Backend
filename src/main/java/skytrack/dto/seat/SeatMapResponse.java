package skytrack.dto.seat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatMapResponse {
    private Long flight_id;
    private List<SeatResponse> seats;
}
