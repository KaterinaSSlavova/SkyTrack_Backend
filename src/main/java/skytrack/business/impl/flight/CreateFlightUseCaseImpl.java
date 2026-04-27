package skytrack.business.impl.flight;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.airport.AirportNotFoundException;
import skytrack.business.exception.flight.FlightStatusNotFoundException;
import skytrack.business.mapper.InternalFlightMapper;
import skytrack.business.service.FlightValidationService;
import skytrack.business.service.TimeConverter;
import skytrack.business.useCase.flight.CreateFlightUseCase;
import skytrack.dto.flight.CreateFlightRequest;
import skytrack.dto.flight.FlightResponse;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.entity.FlightEntity;
import skytrack.persistence.entity.FlightStatusEntity;
import skytrack.persistence.repo.AirportRepository;
import skytrack.persistence.repo.FlightRepository;
import skytrack.persistence.repo.FlightStatusRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CreateFlightUseCaseImpl implements CreateFlightUseCase {
    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final FlightStatusRepository flightStatusRepository;
    private final TimeConverter timeConverter;
    private final FlightValidationService flightValidationService;

    @Override
    public FlightResponse createFlight(CreateFlightRequest request) {
        AirportEntity depAirport = findAirport(request.getDepartureAirportId());
        AirportEntity arrAirport = findAirport(request.getArrivalAirportId());
        FlightStatusEntity status = validateFlightStatus(request.getStatusId());
        Instant depTime = timeConverter.convertToUTC(request.getDepartureLocalTime(), depAirport.getTimezone());
        Instant arrTime = timeConverter.convertToUTC(request.getArrivalLocalTime(), arrAirport.getTimezone());
        FlightEntity flight = InternalFlightMapper.toEntity(request,depAirport,arrAirport,depTime, arrTime, status);
        flightValidationService.validateFlight(flight);
        FlightEntity savedFlight = flightRepository.save(flight);
        return InternalFlightMapper.toResponse(savedFlight,
                timeConverter.convertToLocalTime
                        (savedFlight.getDepartureTimeUTC(),savedFlight.getDepartureAirport().getTimezone()),
                timeConverter.convertToLocalTime
                        (savedFlight.getArrivalTimeUTC(),savedFlight.getArrivalAirport().getTimezone())
                );
    }

    private AirportEntity findAirport(Long id){
        return airportRepository.findById(id).orElseThrow(() -> new AirportNotFoundException(id));
    }

    private FlightStatusEntity validateFlightStatus(Long id){
        return flightStatusRepository.findById(id)
                .orElseThrow(() -> new FlightStatusNotFoundException(id));

    }
}