package skytrack.flight;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import skytrack.business.exception.airport.AirportNotFoundException;
import skytrack.business.exception.booking.CurrencyConversionException;
import skytrack.business.impl.duffel.SaveDuffelUseCaseImpl;
import skytrack.business.mapper.FlightMapper;
import skytrack.business.service.CurrencyConversionService;
import skytrack.business.service.TimeConverter;
import skytrack.dto.duffel.DuffelFlightResponse;
import skytrack.dto.duffel.SavedFlightResponse;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.entity.DuffelFlightEntity;
import skytrack.persistence.enumeration.FlightStatus;
import skytrack.persistence.repo.AirportRepository;
import skytrack.persistence.repo.DuffelRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaveDuffelUseCaseImplTest {

    @Mock
    private DuffelRepository duffelRepository;

    @Mock
    private CurrencyConversionService currencyConversionService;

    @Mock
    private TimeConverter converter;

    @Mock
    private AirportRepository airportRepository;

    @Mock
    private FlightMapper flightMapper;

    @InjectMocks
    private SaveDuffelUseCaseImpl saveDuffelUseCase;

    @Test
    void saveFlight_shouldUpdateExistingFlight_whenFlightAlreadyExists() {
        DuffelFlightResponse response = buildDuffelFlightResponse();

        Instant departureUtc = Instant.parse("2026-06-01T10:00:00Z");
        Instant arrivalUtc = Instant.parse("2026-06-01T12:00:00Z");

        DuffelFlightEntity existingFlight = new DuffelFlightEntity();
        existingFlight.setPrice(new BigDecimal("50.00"));

        SavedFlightResponse savedResponse = mock(SavedFlightResponse.class);

        when(converter.convertToUTC(response.getDepartureLocalTime(), response.getDepartureTimezone()))
                .thenReturn(departureUtc);

        when(converter.convertToUTC(response.getArrivalLocalTime(), response.getArrivalTimezone()))
                .thenReturn(arrivalUtc);

        when(currencyConversionService.convertToEur(response.getPrice(), response.getCurrency()))
                .thenReturn(Mono.just(new BigDecimal("100.00")));

        when(duffelRepository.findByFlightNumberAndDepartureTime(response.getFlightNumber(), departureUtc))
                .thenReturn(Optional.of(existingFlight));

        when(flightMapper.toResponse(existingFlight, response.getDepartureLocalTime(), response.getArrivalLocalTime()))
                .thenReturn(savedResponse);

        SavedFlightResponse result = saveDuffelUseCase.saveFlight(response);

        assertEquals(savedResponse, result);
        assertEquals(new BigDecimal("100.00"), existingFlight.getPrice());
        assertEquals(new BigDecimal("100.00"), response.getPrice());
        assertEquals("EUR", response.getCurrency());

        verify(duffelRepository).save(existingFlight);
        verify(airportRepository, never()).findByIataCode(anyString());
        verify(flightMapper, never()).toEntity(any(), any(), any(), any(), any());
    }

    @Test
    void saveFlight_shouldSaveNewFlight_whenFlightDoesNotExist() {
        DuffelFlightResponse response = buildDuffelFlightResponse();

        Instant departureUtc = Instant.parse("2026-06-01T10:00:00Z");
        Instant arrivalUtc = Instant.parse("2026-06-01T12:00:00Z");

        AirportEntity departureAirport = AirportEntity.builder()
                .iataCode("AMS")
                .build();

        AirportEntity arrivalAirport = AirportEntity.builder()
                .iataCode("LHR")
                .build();

        DuffelFlightEntity entity = new DuffelFlightEntity();
        DuffelFlightEntity savedEntity = new DuffelFlightEntity();

        SavedFlightResponse savedResponse = mock(SavedFlightResponse.class);

        when(converter.convertToUTC(response.getDepartureLocalTime(), response.getDepartureTimezone()))
                .thenReturn(departureUtc);

        when(converter.convertToUTC(response.getArrivalLocalTime(), response.getArrivalTimezone()))
                .thenReturn(arrivalUtc);

        when(currencyConversionService.convertToEur(response.getPrice(), response.getCurrency()))
                .thenReturn(Mono.just(new BigDecimal("100.00")));

        when(duffelRepository.findByFlightNumberAndDepartureTime(response.getFlightNumber(), departureUtc))
                .thenReturn(Optional.empty());

        when(airportRepository.findByIataCode("AMS"))
                .thenReturn(Optional.of(departureAirport));

        when(airportRepository.findByIataCode("LHR"))
                .thenReturn(Optional.of(arrivalAirport));

        when(flightMapper.toEntity(response, departureUtc, arrivalUtc, departureAirport, arrivalAirport))
                .thenReturn(entity);

        when(duffelRepository.save(entity))
                .thenReturn(savedEntity);

        when(flightMapper.toResponse(savedEntity, response.getDepartureLocalTime(), response.getArrivalLocalTime()))
                .thenReturn(savedResponse);

        SavedFlightResponse result = saveDuffelUseCase.saveFlight(response);

        assertEquals(savedResponse, result);
        assertEquals(FlightStatus.SCHEDULED, entity.getStatus());
        assertEquals(new BigDecimal("100.00"), response.getPrice());
        assertEquals("EUR", response.getCurrency());

        verify(airportRepository).findByIataCode("AMS");
        verify(airportRepository).findByIataCode("LHR");
        verify(flightMapper).toEntity(response, departureUtc, arrivalUtc, departureAirport, arrivalAirport);
        verify(duffelRepository).save(entity);
    }

    @Test
    void saveFlight_shouldThrowCurrencyConversionException_whenConversionReturnsNull() {
        DuffelFlightResponse response = buildDuffelFlightResponse();

        when(converter.convertToUTC(response.getDepartureLocalTime(), response.getDepartureTimezone()))
                .thenReturn(Instant.parse("2026-06-01T10:00:00Z"));

        when(converter.convertToUTC(response.getArrivalLocalTime(), response.getArrivalTimezone()))
                .thenReturn(Instant.parse("2026-06-01T12:00:00Z"));

        when(currencyConversionService.convertToEur(response.getPrice(), response.getCurrency()))
                .thenReturn(Mono.empty());

        assertThrows(CurrencyConversionException.class,
                () -> saveDuffelUseCase.saveFlight(response));

        verify(duffelRepository, never()).save(any());
        verify(airportRepository, never()).findByIataCode(anyString());
    }

    @Test
    void saveFlight_shouldThrowAirportNotFoundException_whenDepartureAirportDoesNotExist() {
        DuffelFlightResponse response = buildDuffelFlightResponse();

        Instant departureUtc = Instant.parse("2026-06-01T10:00:00Z");
        Instant arrivalUtc = Instant.parse("2026-06-01T12:00:00Z");

        when(converter.convertToUTC(response.getDepartureLocalTime(), response.getDepartureTimezone()))
                .thenReturn(departureUtc);

        when(converter.convertToUTC(response.getArrivalLocalTime(), response.getArrivalTimezone()))
                .thenReturn(arrivalUtc);

        when(currencyConversionService.convertToEur(response.getPrice(), response.getCurrency()))
                .thenReturn(Mono.just(new BigDecimal("100.00")));

        when(duffelRepository.findByFlightNumberAndDepartureTime(response.getFlightNumber(), departureUtc))
                .thenReturn(Optional.empty());

        when(airportRepository.findByIataCode("AMS"))
                .thenReturn(Optional.empty());

        assertThrows(AirportNotFoundException.class,
                () -> saveDuffelUseCase.saveFlight(response));

        verify(airportRepository).findByIataCode("AMS");
        verify(airportRepository, never()).findByIataCode("LHR");
        verify(duffelRepository, never()).save(any());
    }

    @Test
    void saveFlight_shouldThrowAirportNotFoundException_whenArrivalAirportDoesNotExist() {
        DuffelFlightResponse response = buildDuffelFlightResponse();

        Instant departureUtc = Instant.parse("2026-06-01T10:00:00Z");
        Instant arrivalUtc = Instant.parse("2026-06-01T12:00:00Z");

        AirportEntity departureAirport = AirportEntity.builder()
                .iataCode("AMS")
                .build();

        when(converter.convertToUTC(response.getDepartureLocalTime(), response.getDepartureTimezone()))
                .thenReturn(departureUtc);

        when(converter.convertToUTC(response.getArrivalLocalTime(), response.getArrivalTimezone()))
                .thenReturn(arrivalUtc);

        when(currencyConversionService.convertToEur(response.getPrice(), response.getCurrency()))
                .thenReturn(Mono.just(new BigDecimal("100.00")));

        when(duffelRepository.findByFlightNumberAndDepartureTime(response.getFlightNumber(), departureUtc))
                .thenReturn(Optional.empty());

        when(airportRepository.findByIataCode("AMS"))
                .thenReturn(Optional.of(departureAirport));

        when(airportRepository.findByIataCode("LHR"))
                .thenReturn(Optional.empty());

        assertThrows(AirportNotFoundException.class,
                () -> saveDuffelUseCase.saveFlight(response));

        verify(airportRepository).findByIataCode("AMS");
        verify(airportRepository).findByIataCode("LHR");
        verify(duffelRepository, never()).save(any());
    }

    private DuffelFlightResponse buildDuffelFlightResponse() {
        DuffelFlightResponse response = new DuffelFlightResponse();

        response.setExternalId("offer-123");
        response.setFlightNumber("SK123");
        response.setDepartureIataCode("AMS");
        response.setArrivalIataCode("LHR");
        response.setDepartureLocalTime(LocalDateTime.of(2026, 6, 1, 12, 0));
        response.setArrivalLocalTime(LocalDateTime.of(2026, 6, 1, 14, 0));
        response.setDepartureTimezone("Europe/Amsterdam");
        response.setArrivalTimezone("Europe/London");
        response.setPrice(new BigDecimal("120.00"));
        response.setCurrency("GBP");

        return response;
    }
}
