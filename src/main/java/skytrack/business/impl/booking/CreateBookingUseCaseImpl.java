package skytrack.business.impl.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.flight.FlightNotFoundException;
import skytrack.business.exception.seat.SeatNotAvailableException;
import skytrack.business.exception.seat.SeatNotFoundException;
import skytrack.business.mapper.BookingMapper;
import skytrack.business.mapper.PassengerMapper;
import skytrack.business.service.BookingReferenceGenerator;
import skytrack.business.service.TimeConverter;
import skytrack.business.service.UserService;
import skytrack.business.useCase.booking.CreateBookingUseCase;
import skytrack.dto.booking.BookingResponse;
import skytrack.dto.booking.CreateBookingRequest;
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
    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;
    private final TimeConverter timeConverter;

    @Override
    public BookingResponse toResponse(CreateBookingRequest request) {
        UserEntity loggedUser = userService.getLoggedUser();
        Instant departureTime = timeConverter.convertToUTC(request.getFlight().getDepartureLocalTime(), request.getFlight().getDepartureTimezone());

        DuffelFlightEntity flight = duffelRepository.findByFlightNumberAndDepartureTime(request.getFlight().getFlightNumber(), departureTime)
                .orElseThrow(() -> new FlightNotFoundException(request.getFlight().getExternalId()));

        SeatEntity seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new SeatNotFoundException(request.getSeatId()));

        if(bookingRepository.existsByExternalFlight_IdAndSeat_Id(flight.getId(), seat.getId())){
            throw new SeatNotAvailableException(seat.getSeatNumber());
        }

        BigDecimal totalPrice = calculateTotalPrice(seat, flight.getPrice());
        String bookingReference = referenceGenerator.generate();

        PassengerEntity passenger = passengerMapper.toEntity(request.getPassenger());
        passengerRepository.save(passenger);

        BookingEntity booking = bookingMapper.toEntity(request, loggedUser, flight, passenger, seat, totalPrice, bookingReference);
        return bookingMapper.toResponse(bookingRepository.save(booking));
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
