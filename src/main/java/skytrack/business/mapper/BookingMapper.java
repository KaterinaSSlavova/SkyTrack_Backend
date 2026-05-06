package skytrack.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import skytrack.business.service.TimeConverter;
import skytrack.dto.booking.BookingResponse;
import skytrack.dto.booking.BookingStatus;
import skytrack.dto.booking.CreateBookingRequest;
import skytrack.persistence.entity.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {FlightMapper.class, PassengerMapper.class, SeatMapper.class})
public abstract class BookingMapper {

    @Autowired
    protected TimeConverter timeConverter;

    @Mapping(target="id", ignore = true)
    @Mapping(target="totalPrice", source ="totalPrice")
    @Mapping(target="basePrice", source="flight.price")
    @Mapping(target="externalFlight", source = "flight")
    @Mapping(target="bookingReference", source = "bookingReference")
    @Mapping(target="passenger", source = "passenger")
    public abstract BookingEntity toEntity(CreateBookingRequest request, UserEntity user, DuffelFlightEntity flight, PassengerEntity passenger, SeatEntity seat, BigDecimal totalPrice, String bookingReference);

    @Mapping(target="flight", source="externalFlight")
    @Mapping(target="status", expression = "java(determineStatus(entity))")
    public abstract BookingResponse toResponse(BookingEntity entity);

    protected BookingStatus determineStatus(BookingEntity entity) {
        if(entity.isArchived()) return BookingStatus.CANCELLED;

        LocalDateTime arrival = timeConverter.convertToLocalTime
                (entity.getExternalFlight().getArrivalTime(), entity.getExternalFlight().getArrivalTimezone());

        LocalDateTime now = timeConverter.convertToLocalTime
                (Instant.now(), entity.getExternalFlight().getArrivalTimezone());
        if(arrival.isBefore(now)) return BookingStatus.COMPLETED;

        return BookingStatus.ACTIVE;
    }
}
