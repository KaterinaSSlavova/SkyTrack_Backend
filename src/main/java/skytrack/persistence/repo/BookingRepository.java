package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skytrack.persistence.entity.BookingEntity;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    boolean existsByExternalFlight_IdAndSeat_Id(Long flightId, Long seatId);
    List<BookingEntity> findByExternalFlight_Id(Long flightId);
    BookingEntity getByBookingReference(String reference);
}
