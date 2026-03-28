package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skytrack.persistence.entity.FlightEntity;

import java.util.List;
import java.util.Optional;

public interface JpaFlightRepository extends JpaRepository<FlightEntity, Long>{
}
