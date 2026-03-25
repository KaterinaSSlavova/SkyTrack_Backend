package skytrack.business.repository;

import skytrack.domain.entity.Flight;

import java.util.List;
import java.util.Optional;

public interface FlightRepository {
    Flight saveFlight(Flight flight);
    void updateFlight(Flight flight);
    Optional<Flight> findFlightById(Long flightId);
    void deleteFlight(Long flightId);
    List<Flight> getAllFlights();
    boolean existsById(Long flightId);
}
