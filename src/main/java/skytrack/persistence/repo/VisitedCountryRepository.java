package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skytrack.persistence.entity.VisitedCountryEntity;

import java.util.List;
import java.util.Optional;

public interface VisitedCountryRepository extends JpaRepository<VisitedCountryEntity, Long> {
    Optional<VisitedCountryEntity> findByCountryCodeAndUserId(String countryCode, Long userId);
    boolean existsByCountryCodeAndUserId(String countryCode, Long userId);
    List<VisitedCountryEntity> findByUserId(Long userId);
}
