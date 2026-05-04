package skytrack.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import skytrack.persistence.enumeration.Gender;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "passenger")
public class PassengerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name="passport_number")
    private String passportNumber;

    @Column(name="date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name="passport_expiry")
    private LocalDate passportExpiry;

    private String nationality;
}
