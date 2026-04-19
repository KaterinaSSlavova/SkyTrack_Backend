package skytrack.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="booking")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="flight_id")
    private FlightEntity flight;

    @Column(name="base_price")
    private BigDecimal basePrice;

    @Column(name="total_price")
    private BigDecimal totalPrice;

    @Column(name="booking_reference")
    private String bookingReference;
}
