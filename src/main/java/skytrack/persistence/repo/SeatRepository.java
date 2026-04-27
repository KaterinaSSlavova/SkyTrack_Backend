package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skytrack.persistence.entity.SeatEntity;

public interface SeatRepository extends JpaRepository<SeatEntity, Long> {
}
