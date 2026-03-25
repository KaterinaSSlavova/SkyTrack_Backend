package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skytrack.persistence.entity.FlightStatusEntity;

public interface JpaFlightStatusRepository extends JpaRepository<FlightStatusEntity, Long> {
}
