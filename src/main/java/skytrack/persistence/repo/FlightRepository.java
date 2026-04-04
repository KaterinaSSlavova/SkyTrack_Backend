package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skytrack.persistence.entity.FlightEntity;

public interface FlightRepository extends JpaRepository<FlightEntity, Long>{
}
