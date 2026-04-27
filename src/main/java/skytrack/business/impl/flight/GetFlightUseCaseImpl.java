package skytrack.business.impl.flight;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.flight.FlightNotFoundException;
import skytrack.business.mapper.InternalFlightMapper;
import skytrack.business.service.TimeConverter;
import skytrack.business.useCase.flight.GetFlightUseCase;
import skytrack.dto.flight.FlightResponse;
import skytrack.persistence.entity.FlightEntity;
import skytrack.persistence.repo.FlightRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GetFlightUseCaseImpl implements GetFlightUseCase {
    private final FlightRepository flightRepository;
    private final TimeConverter timeConverter;

    @Override
    public FlightResponse getFlightById(Long flightId){
        FlightEntity flight = validateFlight(flightId);
        LocalDateTime depTime = timeConverter.convertToLocalTime(flight.getDepartureTimeUTC(), flight.getDepartureAirport().getTimezone());
        LocalDateTime arrTime = timeConverter.convertToLocalTime(flight.getArrivalTimeUTC(), flight.getArrivalAirport().getTimezone());
        return InternalFlightMapper.toResponse(flight, depTime, arrTime);
    }

    private FlightEntity validateFlight(Long id){
        return flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException(id));
    }
}
