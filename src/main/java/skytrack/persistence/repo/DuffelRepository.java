package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skytrack.persistence.entity.DuffelFlightEntity;

import java.util.Optional;

public interface DuffelRepository extends JpaRepository<DuffelFlightEntity, Long> {
    Optional<DuffelFlightEntity> findByExternalId(String externalId);
}
