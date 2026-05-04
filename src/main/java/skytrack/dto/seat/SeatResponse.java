package skytrack.dto.seat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatResponse {
    private Long id;
    private String seatNumber;
    private Boolean window;
    private Boolean aisle;
    private Boolean extraLegroom;
    private Boolean available;
}
