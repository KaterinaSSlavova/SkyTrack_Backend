package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import skytrack.persistence.entity.AirportEntity;

import java.util.List;
import java.util.Optional;

public interface AirportRepository extends JpaRepository<AirportEntity, Long> {
    List<AirportEntity> findByIsArchivedFalse();
    Optional<AirportEntity> findByIdAndIsArchivedFalse(Long id);
    boolean existsByIataCode(String iataCode);
    Optional<AirportEntity> findByIataCode(String iataCode);

    @Query("""
          Select a
          From AirportEntity a
          Where Lower(a.name) like Lower (concat('%', :input, '%'))
                or  Lower(a.city) like Lower (concat('%', :input, '%'))
                or Lower(a.country) like Lower(concat('%', :input, '%'))
                or Lower(a.iataCode) like Lower(concat('%', :input, '%'))
          """)
    List<AirportEntity> searchAirports(String input);
}
