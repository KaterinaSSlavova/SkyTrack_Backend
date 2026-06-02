package skytrack.dto.booking;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingStatsResponse {
    private long totalFlights;
    private BigDecimal totalSpent;
    private long uniqueDestinations;
    private long upcomingFlights;
}