package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skytrack.persistence.entity.PassengerEntity;

public interface PassengerRepository extends JpaRepository<PassengerEntity, Long> {
}
