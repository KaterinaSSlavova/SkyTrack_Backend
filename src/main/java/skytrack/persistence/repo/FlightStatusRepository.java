package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skytrack.persistence.entity.FlightStatusEntity;

import java.util.Optional;

public interface FlightStatusRepository extends JpaRepository<FlightStatusEntity, Long> {
    Optional<FlightStatusEntity> findFlightStatusByName(String  flightStatusName);
}
