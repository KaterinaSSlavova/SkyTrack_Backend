package skytrack.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingStatsResponse {
    private long totalFlights;
    private BigDecimal totalSpent;
    private long uniqueDestinations;
    private long upcomingFlights;
}