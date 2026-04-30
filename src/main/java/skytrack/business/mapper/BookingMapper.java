package skytrack.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import skytrack.dto.booking.BookingResponse;
import skytrack.dto.booking.CreateBookingRequest;
import skytrack.persistence.entity.*;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = {FlightMapper.class, PassengerMapper.class, SeatMapper.class})
public interface BookingMapper {

    @Mapping(target="id", ignore = true)
    @Mapping(target="totalPrice", source ="totalPrice")
    @Mapping(target="basePrice", source="flight.price")
    @Mapping(target="externalFlight", source = "flight")
    @Mapping(target="bookingReference", source = "bookingReference")
    @Mapping(target="passenger", source = "passenger")
    @Mapping(target="archived", ignore = true)
    BookingEntity toEntity(CreateBookingRequest request, UserEntity user, DuffelFlightEntity flight, PassengerEntity passenger, SeatEntity seat, BigDecimal totalPrice, String bookingReference);

    @Mapping(target="flight", source="externalFlight")
    BookingResponse toResponse(BookingEntity entity);
}
