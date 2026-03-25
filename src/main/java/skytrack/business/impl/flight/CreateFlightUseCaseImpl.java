package skytrack.business.impl.flight;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.AirportNotFoundException;
import skytrack.business.exception.FlightStatusNotFoundException;
import skytrack.business.mapper.FlightMapper;
import skytrack.business.repository.AirportRepository;
import skytrack.business.repository.FlightRepository;
import skytrack.business.repository.FlightStatusRepository;
import skytrack.business.service.TimeConverter;
import skytrack.business.useCase.flight.CreateFlightUseCase;
import skytrack.domain.entity.Airport;
import skytrack.domain.entity.Flight;
import skytrack.domain.entity.FlightStatus;
import skytrack.dto.flight.CreateFlightRequest;
import skytrack.dto.flight.FlightResponse;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CreateFlightUseCaseImpl implements CreateFlightUseCase {
    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final FlightStatusRepository flightStatusRepository;
    private final TimeConverter timeConverter;

    @Override
    public FlightResponse createFlight(CreateFlightRequest request) {
        Airport depAirport = validateAirport(request.getDepartureAirportId());
        Airport arrAirport = validateAirport(request.getArrivalAirportId());
        FlightStatus status = validateFlightStatus(request.getStatusId());
        Instant depTime = timeConverter.convertToUTC(request.getDepartureLocalTime(), depAirport.getTimezone());
        Instant arrTime = timeConverter.convertToUTC(request.getArrivalLocalTime(), arrAirport.getTimezone());
        Flight flight = FlightMapper.toDomain(request,depAirport,arrAirport,depTime, arrTime, status);
        Flight savedFlight = flightRepository.saveFlight(flight);
        return FlightMapper.toResponse(savedFlight,
                timeConverter.convertToLocalTime
                        (savedFlight.getDepartureTimeUTC(),savedFlight.getDepartureAirport().getTimezone()),
                timeConverter.convertToLocalTime
                        (savedFlight.getArrivalTimeUTC(),savedFlight.getArrivalAirport().getTimezone())
                );
    }

    private Airport validateAirport(Long id){
        return airportRepository.getAirportById(id)
                .orElseThrow(() -> new AirportNotFoundException(id));
    }

    private FlightStatus validateFlightStatus(Long id){
        return flightStatusRepository.getFlightStatusById(id)
                .orElseThrow(() -> new FlightStatusNotFoundException(id));
    }
}