package skytrack.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String picture;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    private LocalDate birthdate;
    private String email;

    @Column(name="password_hash")
    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;
}
