package skytrack.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "external_flight")
public class DuffelFlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name="external_id")
    private String externalId;

    @Column(name="flight_number")
    private String flightNumber;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name="departure_iata_code", columnDefinition = "char(3)")
    private String departureIataCode;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name="arrival_iata_code", columnDefinition = "char(3)")
    private String arrivalIataCode;

    @Column(name="departure_time")
    private Instant departureTime;

    @Column(name="arrival_time")
    private Instant arrivalTime;

    @Column(name="departure_timezone")
    private String departureTimezone;

    @Column(name="arrival_timezone")
    private String arrivalTimezone;
    private BigDecimal price;
    private String currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="departure_airport_id")
    private AirportEntity departureAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="arrival_airport_id")
    private AirportEntity arrivalAirport;
}
