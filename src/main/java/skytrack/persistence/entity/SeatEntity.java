package skytrack.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="seat")
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="seat_number")
    private Long seatNumber;

    @Column(name="is_window")
    private Boolean window;

    @Column(name="is_aisle")
    private Boolean aisle;

    @Column(name="is_extra_legroom")
    private Boolean extraLegroom;
}
