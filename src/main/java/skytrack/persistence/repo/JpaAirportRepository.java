package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skytrack.persistence.entity.AirportEntity;

public interface JpaAirportRepository extends JpaRepository<AirportEntity, Long> {
}
