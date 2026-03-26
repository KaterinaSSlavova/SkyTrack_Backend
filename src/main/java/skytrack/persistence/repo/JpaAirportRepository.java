package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skytrack.persistence.entity.AirportEntity;

import java.util.List;
import java.util.Optional;

public interface JpaAirportRepository extends JpaRepository<AirportEntity, Long> {
    List<AirportEntity> findByIsArchivedFalse();
    Optional<AirportEntity> findByIdAndIsArchivedFalse(Long id);
}
