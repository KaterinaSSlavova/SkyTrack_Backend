package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skytrack.persistence.entity.BookingEntity;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
}
