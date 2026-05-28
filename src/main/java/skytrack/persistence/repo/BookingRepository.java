package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import skytrack.persistence.entity.BookingEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    boolean existsByExternalFlight_IdAndSeat_Id(Long flightId, Long seatId);
    List<BookingEntity> findByExternalFlight_Id(Long flightId);
    BookingEntity getByBookingReference(String reference);
    long countByUser_Id(Long userId);

    @Query("""
              SELECT COALESCE(SUM(b.totalPrice), 0)
                            FROM BookingEntity b WHERE b.user.id = :userId
              """)
    BigDecimal sumTotalPriceByUserId(Long userId);

    @Query("""
               SELECT COUNT(DISTINCT b.externalFlight.arrivalIataCode)
                              FROM BookingEntity b WHERE b.user.id = :userId
               """)
    long countDistinctDestinationsByUserId(Long userId);

    @Query("""
               SELECT COUNT(b) FROM BookingEntity b
                              WHERE b.user.id = :userId AND b.externalFlight.departureTime > :now
            """)
    long countUpcomingByUserId(Long userId, Instant now);

    @Query("""
               SELECT b FROM BookingEntity b WHERE b.user.id = :userId AND
                              b.externalFlight.departureTime > :now ORDER BY
                                             b.externalFlight.departureTime ASC LIMIT 1
               """)
    Optional<BookingEntity> findNextUpcoming(Long userId, Instant now);
}
