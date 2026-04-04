package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skytrack.persistence.entity.AirportEntity;

import java.util.List;
import java.util.Optional;

public interface AirportRepository extends JpaRepository<AirportEntity, Long> {
    List<AirportEntity> findByIsArchivedFalse();
    Optional<AirportEntity> findByIdAndIsArchivedFalse(Long id);
    boolean existsByIataCode(String iataCode);
}
