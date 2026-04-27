package skytrack.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flight")
public class FlightEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_number")
    private String flightNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dep_airport_id")
    private AirportEntity departureAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="arr_airport_id")
    private AirportEntity arrivalAirport;

    @Column(name = "dep_time_utc")
    private Instant departureTimeUTC;

    @Column(name = "arr_time_utc")
    private Instant arrivalTimeUTC;
    private String gate;
    private String terminal;
    private Integer capacity;
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_status_id")
    private FlightStatusEntity status;

    @Column(name ="created_at")
    private Instant createdAt;
}
