package skytrack.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Setter(AccessLevel.NONE)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "expiry_date")
    private Instant expiryDate;

    @PrePersist
    private void setToken(){
        if(token == null){
            token = UUID.randomUUID().toString();
        }
    }
}
