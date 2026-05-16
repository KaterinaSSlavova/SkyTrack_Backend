package skytrack.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.flight.FlightNotFoundException;
import skytrack.business.exception.seat.SeatNotAvailableException;
import skytrack.business.exception.seat.SeatNotFoundException;
import skytrack.business.impl.booking.CreateBookingUseCaseImpl;
import skytrack.business.mapper.BookingMapper;
import skytrack.business.mapper.PassengerMapper;
import skytrack.business.service.BookingReferenceGenerator;
import skytrack.business.service.TimeConverter;
import skytrack.business.service.UserService;
import skytrack.dto.booking.BookingResponse;
import skytrack.dto.booking.CreateBookingRequest;
import skytrack.dto.duffel.DuffelFlightResponse;
import skytrack.persistence.entity.*;
import skytrack.persistence.repo.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CreateBookingUseCaseImplTest {
    @Mock
    private UserService userService;

    @Mock
    private DuffelRepository duffelRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ExtrasRepository extrasRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private BookingReferenceGenerator referenceGenerator;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private PassengerMapper passengerMapper;

    @Mock
    private TimeConverter timeConverter;

    @InjectMocks
    private CreateBookingUseCaseImpl createBooking;

    private CreateBookingRequest buildRequest() {
        DuffelFlightResponse flight = new DuffelFlightResponse();
        flight.setFlightNumber("SK123");
        flight.setDepartureLocalTime(LocalDateTime.now());
        flight.setDepartureTimezone("Europe/Amsterdam");
        flight.setExternalId("ext-123");

        CreateBookingRequest request = new CreateBookingRequest();
        request.setFlight(flight);
        request.setSeatId(1L);
        return request;
    }

    @Test
    void createBooking_shouldThrowFlightNotFoundException_whenFlightDoesNotExist() {
        //arrange
        when(timeConverter.convertToUTC(any(), any())).thenReturn(Instant.now());
        when(duffelRepository.findByFlightNumberAndDepartureTime(any(), any())).thenReturn(Optional.empty());

        //act and assert
        assertThrows(FlightNotFoundException.class, () -> createBooking.toResponse(buildRequest()));
    }

    @Test
    void createBooking_shouldThrowSeatNotFoundException_whenSeatDoesNotExist() {
        //arrange
        when(timeConverter.convertToUTC(any(), any())).thenReturn(Instant.now());
        when(duffelRepository.findByFlightNumberAndDepartureTime(any(), any())).thenReturn(Optional.of(new DuffelFlightEntity()));
        when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        //act and assert
        assertThrows(SeatNotFoundException.class, () -> createBooking.toResponse(buildRequest()));
    }

    @Test
    void createBooking_shouldThrowSeatNotAvailableException_whenSeatWasBooked() {
        //arrange
        when(timeConverter.convertToUTC(any(), any())).thenReturn(Instant.now());
        when(duffelRepository.findByFlightNumberAndDepartureTime(any(), any())).thenReturn(Optional.of(new DuffelFlightEntity()));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(new SeatEntity()));
        when(bookingRepository.existsByExternalFlight_IdAndSeat_Id(any(), any())).thenReturn(true);

        //act and assert
        assertThrows(SeatNotAvailableException.class, () -> createBooking.toResponse(buildRequest()));
    }

    @Test
    void createBooking_shouldAddExtraPrice_whenWindowSeatAndExtraLegRoom(){
        //arrange
        SeatEntity seat = new SeatEntity(1L, "", true, false, true);
        DuffelFlightEntity flight = new DuffelFlightEntity();
        flight.setPrice(new BigDecimal("100"));
        ExtrasEntity windowExtra = new ExtrasEntity();
        windowExtra.setPrice(new BigDecimal("15.00"));
        ExtrasEntity legroomExtra = new ExtrasEntity();
        legroomExtra.setPrice(new BigDecimal("20.00"));
        BookingEntity bookingEntity = new BookingEntity();
        BookingResponse expectedResponse = new BookingResponse();

        when(userService.getLoggedUser()).thenReturn(new UserEntity());
        when(timeConverter.convertToUTC(any(), any())).thenReturn(Instant.now());
        when(duffelRepository.findByFlightNumberAndDepartureTime(any(), any())).thenReturn(Optional.of(flight));
        when(seatRepository.findById(any())).thenReturn(Optional.of(seat));
        when(bookingRepository.existsByExternalFlight_IdAndSeat_Id(any(), any())).thenReturn(false);
        when(extrasRepository.findByName("window")).thenReturn(windowExtra);
        when(extrasRepository.findByName("extra_legroom")).thenReturn(legroomExtra);
        when(referenceGenerator.generate()).thenReturn("SKY-ABC123");
        when(passengerMapper.toEntity(any())).thenReturn(new PassengerEntity());
        when(passengerRepository.save(any())).thenReturn(new PassengerEntity());
        when(bookingMapper.toEntity(any(), any(), any(), any(), any(), any(), any())).thenReturn(bookingEntity);
        when(bookingRepository.save(any())).thenReturn(bookingEntity);
        when(bookingMapper.toResponse(any())).thenReturn(expectedResponse);

        //act
        BookingResponse result = createBooking.toResponse(buildRequest());

        //assert
        verify(extrasRepository).findByName("window");
        verify(extrasRepository).findByName("extra_legroom");
        assertNotNull(result);
    }

    @Test
    void createBooking_shouldAddWindowPrice_whenWindowSeatOnly() {
        // arrange
        SeatEntity seat = new SeatEntity(1L, "A2", true, false, false);
        DuffelFlightEntity flight = new DuffelFlightEntity();
        flight.setPrice(new BigDecimal("100"));

        ExtrasEntity windowExtra = new ExtrasEntity();
        windowExtra.setPrice(new BigDecimal("15.00"));

        BookingEntity bookingEntity = new BookingEntity();
        BookingResponse expectedResponse = new BookingResponse();

        when(userService.getLoggedUser()).thenReturn(new UserEntity());
        when(timeConverter.convertToUTC(any(), any())).thenReturn(Instant.now());
        when(duffelRepository.findByFlightNumberAndDepartureTime(any(), any())).thenReturn(Optional.of(flight));
        when(seatRepository.findById(any())).thenReturn(Optional.of(seat));
        when(bookingRepository.existsByExternalFlight_IdAndSeat_Id(any(), any())).thenReturn(false);
        when(extrasRepository.findByName("window")).thenReturn(windowExtra);
        when(referenceGenerator.generate()).thenReturn("SKY-ABC123");
        when(passengerMapper.toEntity(any())).thenReturn(new PassengerEntity());
        when(passengerRepository.save(any())).thenReturn(new PassengerEntity());
        when(bookingMapper.toEntity(any(), any(), any(), any(), any(), any(), any())).thenReturn(bookingEntity);
        when(bookingRepository.save(any())).thenReturn(bookingEntity);
        when(bookingMapper.toResponse(any())).thenReturn(expectedResponse);

        // act
        BookingResponse result = createBooking.toResponse(buildRequest());

        // assert
        verify(extrasRepository).findByName("window");
        assertNotNull(result);
    }
}
