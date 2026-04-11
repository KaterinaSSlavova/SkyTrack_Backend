package skytrack.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import skytrack.persistence.enumeration.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="role_name")
    private Role roleName;
}
