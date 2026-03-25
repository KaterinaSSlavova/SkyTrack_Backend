package skytrack.business.impl.flight;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.FlightNotFoundException;
import skytrack.business.mapper.FlightMapper;
import skytrack.business.repository.FlightRepository;
import skytrack.business.service.TimeConverter;
import skytrack.business.useCase.flight.GetFlightUseCase;
import skytrack.domain.entity.Flight;
import skytrack.dto.flight.FlightResponse;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GetFlightUseCaseImpl implements GetFlightUseCase {
    private final FlightRepository flightRepository;
    private final TimeConverter timeConverter;

    @Override
    public FlightResponse getFlightById(Long flightId){
        Flight flight = validateFlight(flightId);
        LocalDateTime depTime = timeConverter.convertToLocalTime(flight.getDepartureTimeUTC(), flight.getDepartureAirport().getTimezone());
        LocalDateTime arrTime = timeConverter.convertToLocalTime(flight.getArrivalTimeUTC(), flight.getArrivalAirport().getTimezone());
        return FlightMapper.toResponse(flight, depTime, arrTime);
    }

    private Flight validateFlight(Long id){
        return flightRepository.findFlightById(id).orElseThrow(() -> new FlightNotFoundException(id));
    }
}
