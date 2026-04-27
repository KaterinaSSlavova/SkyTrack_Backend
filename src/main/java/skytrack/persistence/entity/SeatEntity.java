package skytrack.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="seat")
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name="seat_number")
    private String seatNumber;

    @Column(name="is_window")
    private Boolean window;

    @Column(name="is_aisle")
    private Boolean aisle;

    @Column(name="is_extra_legroom")
    private Boolean extraLegroom;
}
