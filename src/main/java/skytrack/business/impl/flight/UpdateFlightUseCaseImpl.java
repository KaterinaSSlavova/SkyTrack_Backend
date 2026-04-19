package skytrack.business.impl.flight;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.airport.AirportNotFoundException;
import skytrack.business.exception.flight.FlightNotFoundException;
import skytrack.business.exception.flight.FlightStatusNotFoundException;
import skytrack.business.mapper.FlightMapper;
import skytrack.business.service.FlightValidationService;
import skytrack.business.service.TimeConverter;
import skytrack.business.useCase.flight.UpdateFlightUseCase;
import skytrack.dto.flight.UpdateFlightRequest;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.entity.FlightEntity;
import skytrack.persistence.entity.FlightStatusEntity;
import skytrack.persistence.repo.AirportRepository;
import skytrack.persistence.repo.FlightRepository;
import skytrack.persistence.repo.FlightStatusRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UpdateFlightUseCaseImpl implements UpdateFlightUseCase {
    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final TimeConverter timeConverter;
    private final FlightStatusRepository fStatusRepository;
    private final FlightValidationService flightValidationService;

    @Override
    public void updateFlight(UpdateFlightRequest request) {
       FlightEntity existingFlight = findFlight(request.getId());
       AirportEntity depAirport = request.getDepartureAirportId() !=null ?
               findAirport(request.getDepartureAirportId()) : existingFlight.getDepartureAirport();

       AirportEntity arrAirport = request.getArrivalAirportId() != null ?
               findAirport(request.getArrivalAirportId()) : existingFlight.getArrivalAirport();

       Instant depTimeUTC = request.getDepartureLocalTime() != null
                ? timeConverter.convertToUTC(request.getDepartureLocalTime(), depAirport.getTimezone())
                : existingFlight.getDepartureTimeUTC();

       Instant arrTimeUTC = request.getArrivalLocalTime() != null
                ? timeConverter.convertToUTC(request.getArrivalLocalTime(), arrAirport.getTimezone())
                : existingFlight.getArrivalTimeUTC();

       FlightStatusEntity status = request.getStatusId() !=null ?
               findFlightStatus(request.getStatusId()) : existingFlight.getStatus();

       FlightMapper.updateEntity(existingFlight, request, depAirport, arrAirport, depTimeUTC, arrTimeUTC, status);
       flightValidationService.validateFlight(existingFlight);
       flightRepository.save(existingFlight);
    }

    private FlightEntity findFlight(Long id) {
       return flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException(id));
    }

    private AirportEntity findAirport(Long id) {
        return airportRepository.findById(id)
                .orElseThrow(() -> new AirportNotFoundException(id));
    }

    private FlightStatusEntity findFlightStatus(Long id){
        return fStatusRepository.findById(id)
                .orElseThrow(() -> new FlightStatusNotFoundException(id));
    }
}