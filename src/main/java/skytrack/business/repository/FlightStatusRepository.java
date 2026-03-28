package skytrack.business.repository;

import skytrack.domain.entity.FlightStatus;

import java.util.List;
import java.util.Optional;

public interface FlightStatusRepository {
    FlightStatus saveFlightStatus(FlightStatus flightStatus);
    Optional<FlightStatus> getFlightStatusById(Long statusId);
    void deleteFlightStatus(Long statusId);
    Optional<FlightStatus> findFlightStatusByName(String flightStatusName);
    List<FlightStatus> getAllFlightStatuses();
    boolean existsById(Long statusId);
}
