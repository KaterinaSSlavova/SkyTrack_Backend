package skytrack.business.impl.duffel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.airport.AirportNotFoundException;
import skytrack.business.exception.booking.CurrencyConversionException;
import skytrack.business.mapper.FlightMapper;
import skytrack.business.service.CurrencyConversionService;
import skytrack.business.service.TimeConverter;
import skytrack.business.useCase.flight.SaveDuffelUseCase;
import skytrack.dto.duffel.DuffelFlightResponse;
import skytrack.dto.duffel.SavedFlightResponse;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.entity.DuffelFlightEntity;
import skytrack.persistence.repo.AirportRepository;
import skytrack.persistence.repo.DuffelRepository;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SaveDuffelUseCaseImpl implements SaveDuffelUseCase {
    private final DuffelRepository duffelRepository;
    private final CurrencyConversionService currencyConversionService;
    private final TimeConverter converter;
    private final AirportRepository airportRepository;
    private final FlightMapper flightMapper;

    @Override
    public SavedFlightResponse saveFlight(DuffelFlightResponse response) {
        Instant departureTime = converter.convertToUTC(response.getDepartureLocalTime(),response.getDepartureTimezone());
        Instant arrivalTime = converter.convertToUTC(response.getArrivalLocalTime(),response.getArrivalTimezone());

        BigDecimal priceInEuro = currencyConversionService.convertToEur(response.getPrice(), response.getCurrency()).block();
        if(priceInEuro == null){
            throw new CurrencyConversionException(response.getExternalId());
        }

        response.setPrice(priceInEuro);
        response.setCurrency("EUR");

        return duffelRepository.findByFlightNumberAndDepartureTime(response.getFlightNumber(), departureTime)
                .map(flight -> {
                    flight.setPrice(priceInEuro);
                    duffelRepository.save(flight);
                   return flightMapper.toResponse(flight, response.getDepartureLocalTime(), response.getArrivalLocalTime());
                })
                .orElseGet(() -> {

                    AirportEntity depAirport = airportRepository.findByIataCode(response.getDepartureIataCode())
                            .orElseThrow(() -> new AirportNotFoundException(response.getDepartureIataCode()));
                    AirportEntity arrAirport = airportRepository.findByIataCode(response.getArrivalIataCode())
                            .orElseThrow(() -> new AirportNotFoundException(response.getArrivalIataCode()));

                    DuffelFlightEntity entity = flightMapper.toEntity(response, departureTime, arrivalTime, depAirport, arrAirport);
                    DuffelFlightEntity savedFlight = duffelRepository.save(entity);
                    return flightMapper.toResponse(savedFlight, response.getDepartureLocalTime(), response.getArrivalLocalTime());
                });
    }
}
