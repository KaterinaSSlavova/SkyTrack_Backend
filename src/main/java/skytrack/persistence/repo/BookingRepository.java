package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skytrack.persistence.entity.BookingEntity;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    boolean existsByExternalFlightIdAndSeatId(Long externalFlightId, Long seatId);
    List<BookingEntity> findByExternalFlightId(Long flightId);
}
