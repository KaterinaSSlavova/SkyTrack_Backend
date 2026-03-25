package skytrack.business.repository;

import skytrack.domain.entity.Airport;

import java.util.List;
import java.util.Optional;

public interface AirportRepository {
    Airport saveAirport(Airport airport);
    void updateAirport(Airport airport);
    Optional<Airport> getAirportById(Long airportId);
    void deleteAirport(Long airportId);
    List<Airport> getAllAirports();
    boolean existsById(Long id);
}
