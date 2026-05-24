package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skytrack.persistence.entity.RefreshToken;
import skytrack.persistence.entity.UserEntity;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
     void deleteByUser(UserEntity user);
     boolean existsByUser(UserEntity user);
     Optional<RefreshToken> findByToken(String token);
     void deleteByToken(String token);
}
