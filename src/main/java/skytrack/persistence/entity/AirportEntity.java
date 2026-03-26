package skytrack.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "airport")
public class AirportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "iata_code", length = 3, columnDefinition = "char(3)")
    private String iataCode;

    private String name;
    private String city;
    private String country;
    private String timezone;

    @Column(name="is_archived")
    private Boolean isArchived;
}
