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
@Builder
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

    @Builder.Default
    @Column(name = "is_archived", nullable = false)
    private Boolean isArchived = false;
}
