package skytrack.business.impl.flight;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.mapper.FlightMapper;
import skytrack.business.service.TimeConverter;
import skytrack.business.useCase.flight.GetAllFlightsUseCase;
import skytrack.domain.entity.Flight;
import skytrack.dto.flight.FlightResponse;
import skytrack.dto.flight.GetAllFlightsResponse;
import skytrack.persistence.entity.FlightEntity;
import skytrack.persistence.repo.FlightRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllFlightsUseCaseImpl implements GetAllFlightsUseCase {
    private final FlightRepository flightRepository;
    private final TimeConverter timeConverter;

    @Override
    public GetAllFlightsResponse getAllFlights() {
        List<FlightEntity> flights = flightRepository.findAll();
        List<FlightResponse> response= flights.stream().map(this::convertFlightToResponse).toList();
        return new GetAllFlightsResponse(response);
    }

    private FlightResponse convertFlightToResponse(FlightEntity flight) {
        return FlightMapper.toResponse(flight,
                timeConverter.convertToLocalTime
                        (flight.getDepartureTimeUTC(), flight.getDepartureAirport().getTimezone()),
                timeConverter.convertToLocalTime
                        (flight.getArrivalTimeUTC(), flight.getArrivalAirport().getTimezone()));
    }
}
