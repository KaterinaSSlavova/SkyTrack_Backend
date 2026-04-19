package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.entity.FlightEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<FlightEntity, Long>{
    @Query("""
          Select f
          From FlightEntity f  
          Where f.departureAirport.id = :depAirportId
             and f.arrivalAirport.id = :arrAirportId
             and f.departureTimeUTC >= :startOfDayUTC
             and f.departureTimeUTC < :endOfDayUTC
             and f.status.name <> 'CANCELLED'
          """)
    List<FlightEntity> searchFlights(@Param("depAirportId") long depAirportId,
                                     @Param("arrAirportId") long arrAirportId,
                                     @Param("startOfDayUTC") Instant startOfDayUTC,
                                     @Param("endOfDayUTC") Instant endOfDayUTC);
}
