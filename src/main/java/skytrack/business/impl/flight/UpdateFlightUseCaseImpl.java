package skytrack.business.impl.flight;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.AirportNotFoundException;
import skytrack.business.exception.FlightNotFoundException;
import skytrack.business.exception.FlightStatusNotFoundException;
import skytrack.business.mapper.FlightMapper;
import skytrack.business.repository.AirportRepository;
import skytrack.business.repository.FlightRepository;
import skytrack.business.repository.FlightStatusRepository;
import skytrack.business.service.TimeConverter;
import skytrack.business.useCase.flight.UpdateFlightUseCase;
import skytrack.domain.entity.Airport;
import skytrack.domain.entity.Flight;
import skytrack.domain.entity.FlightStatus;
import skytrack.dto.flight.UpdateFlightRequest;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UpdateFlightUseCaseImpl implements UpdateFlightUseCase {
    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final TimeConverter timeConverter;
    private final FlightStatusRepository fStatusRepository;

    @Override
    public void updateFlight(UpdateFlightRequest request) {
       validateFlight(request.getId());
       Airport depAirport = findAirport(request.getDepartureAirportId());
       Airport arrAirport = findAirport(request.getArrivalAirportId());
       Instant depTimeUTC = timeConverter.convertToUTC(request.getDepartureLocalTime(), depAirport.getTimezone());
       Instant arrTimeUTC = timeConverter.convertToUTC(request.getArrivalLocalTime(), arrAirport.getTimezone());
       FlightStatus status = findFlightStatus(request.getStatusId());

       Flight updatedFlight = FlightMapper.toDomain(request, depAirport, arrAirport, depTimeUTC, arrTimeUTC, status);
       flightRepository.updateFlight(updatedFlight);
    }

    private void validateFlight(Long id) {
       if(!flightRepository.existsById(id)) {
           throw new FlightNotFoundException(id);
       }
    }

    private Airport findAirport(Long id){
        return airportRepository.getAirportById(id)
                .orElseThrow(() -> new AirportNotFoundException(id));
    }

    private FlightStatus findFlightStatus(Long id){
        return fStatusRepository.getFlightStatusById(id)
                .orElseThrow(() -> new FlightStatusNotFoundException(id));
    }
}