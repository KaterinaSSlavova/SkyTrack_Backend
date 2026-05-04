package skytrack.business.impl.flight;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.airport.AirportNotFoundException;
import skytrack.business.mapper.InternalFlightMapper;
import skytrack.business.service.TimeConverter;
import skytrack.business.useCase.flight.SearchFlightUseCase;
import skytrack.dto.flight.FlightResponse;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.entity.FlightEntity;
import skytrack.persistence.repo.AirportRepository;
import skytrack.persistence.repo.FlightRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchFlightUseCaseImpl implements SearchFlightUseCase {
    private final AirportRepository airportRepository;
    private final TimeConverter timeConverter;
    private final FlightRepository flightRepository;

    @Override
    public List<FlightResponse> searchFlights(String departureIata, String arrivalIata, LocalDate departureDate) {
        AirportEntity departureAirport = airportRepository.findByIataCode(departureIata)
                .orElseThrow(() -> new AirportNotFoundException(departureIata));

        AirportEntity arrivalAirport = airportRepository.findByIataCode(arrivalIata)
                .orElseThrow(() -> new AirportNotFoundException(arrivalIata));

        Instant startOfDay = timeConverter.getStartOfDayUTC(departureDate, departureAirport.getTimezone());
        Instant endOfDay = timeConverter.getEndOfDayUTC(departureDate, departureAirport.getTimezone());

        List<FlightEntity> flights = flightRepository.searchFlights
                (departureAirport.getId(), arrivalAirport.getId(), startOfDay, endOfDay);

        return flights.stream().map(this::convertFlightToResponse).toList();
    }

    private FlightResponse convertFlightToResponse(FlightEntity flight) {
        return InternalFlightMapper.toResponse(flight,
                timeConverter.convertToLocalTime
                        (flight.getDepartureTimeUTC(), flight.getDepartureAirport().getTimezone()),
                timeConverter.convertToLocalTime
                        (flight.getArrivalTimeUTC(), flight.getArrivalAirport().getTimezone()));
    }
}
