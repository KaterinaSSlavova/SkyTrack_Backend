package skytrack.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="booking")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id")
    private PassengerEntity passenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "external_flight_id")
    private DuffelFlightEntity externalFlight;

    @Column(name="base_price")
    private BigDecimal basePrice;

    @Column(name="total_price")
    private BigDecimal totalPrice;

    private String currency;

    @Column(name="booking_reference")
    private String bookingReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seat_id")
    private SeatEntity seat;

    @Column(name="is_archived")
    private boolean archived;
}
