package skytrack.business.impl.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.airport.AirportNotFoundException;
import skytrack.business.exception.booking.CurrencyConversionException;
import skytrack.business.exception.seat.SeatNotAvailableException;
import skytrack.business.exception.seat.SeatNotFoundException;
import skytrack.business.mapper.BookingMapper;
import skytrack.business.mapper.FlightMapper;
import skytrack.business.service.BookingReferenceGenerator;
import skytrack.business.service.CurrencyConversionService;
import skytrack.business.service.TimeConverter;
import skytrack.business.service.UserService;
import skytrack.business.useCase.booking.CreateBookingUseCase;
import skytrack.dto.booking.BookingResponse;
import skytrack.dto.booking.CreateBookingRequest;
import skytrack.dto.duffel.DuffelFlightResponse;
import skytrack.persistence.entity.*;
import skytrack.persistence.repo.*;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CreateBookingUseCaseImpl implements CreateBookingUseCase {
    private final UserService userService;
    private final DuffelRepository duffelRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final ExtrasRepository extrasRepository;
    private final BookingMapper bookingMapper;
    private final BookingReferenceGenerator referenceGenerator;
    private final AirportRepository airportRepository;
    private final TimeConverter converter;
    private final FlightMapper flightMapper;
    private final CurrencyConversionService currencyConversionService;

    @Override
    public BookingResponse toResponse(CreateBookingRequest request) {
        UserEntity loggedUser = userService.getLoggedUser();
        DuffelFlightEntity flight = duffelRepository.findByExternalId(request.getFlight().getExternalId())
                .orElseGet(() -> saveFlight(request.getFlight()));

        SeatEntity seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new SeatNotFoundException(request.getSeatId()));

        if(bookingRepository.existsByExternalFlightIdAndSeatId(flight.getId(), seat.getId())){
            throw new SeatNotAvailableException(seat.getSeatNumber());
        }

        BigDecimal totalPrice = calculateTotalPrice(seat, flight.getPrice());
        String bookingReference = referenceGenerator.generate();

        BookingEntity booking = bookingMapper.toEntity(request, loggedUser, flight, seat, totalPrice, bookingReference);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    private DuffelFlightEntity saveFlight(DuffelFlightResponse response){
        BigDecimal priceInEuro = currencyConversionService.convertToEur(response.getPrice(), response.getCurrency()).block();
        if(priceInEuro == null){
            throw new CurrencyConversionException(response.getExternalId());
        }

        response.setPrice(priceInEuro);
        response.setCurrency("EUR");

        Instant departureTime = converter.convertToUTC(response.getDepartureLocalTime(),response.getDepartureTimezone());
        Instant arrivalTime = converter.convertToUTC(response.getArrivalLocalTime(),response.getArrivalTimezone());
        AirportEntity depAirport = airportRepository.findByIataCode(response.getDepartureIataCode())
                .orElseThrow(() -> new AirportNotFoundException(response.getDepartureIataCode()));
        AirportEntity arrAirport = airportRepository.findByIataCode(response.getArrivalIataCode())
                .orElseThrow(() -> new AirportNotFoundException(response.getArrivalIataCode()));

        DuffelFlightEntity entity = flightMapper.toEntity(response, departureTime, arrivalTime, depAirport, arrAirport);
        return duffelRepository.save(entity);
    }

    private BigDecimal calculateTotalPrice(SeatEntity seat, BigDecimal flightPrice) {
        BigDecimal total = flightPrice;

        if(seat.getExtraLegroom()){
            BigDecimal price = extrasRepository.findByName("extra_legroom").getPrice();
            total = total.add(price);
        }

        if(seat.getWindow()){
            BigDecimal price = extrasRepository.findByName("window").getPrice();
            total = total.add(price);
        }
        return total;
    }
}
