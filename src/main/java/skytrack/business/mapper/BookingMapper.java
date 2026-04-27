package skytrack.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import skytrack.dto.booking.BookingResponse;
import skytrack.dto.booking.CreateBookingRequest;
import skytrack.persistence.entity.BookingEntity;
import skytrack.persistence.entity.DuffelFlightEntity;
import skytrack.persistence.entity.SeatEntity;
import skytrack.persistence.entity.UserEntity;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = {FlightMapper.class, PassengerMapper.class, SeatMapper.class})
public interface BookingMapper {

    @Mapping(target="id", ignore = true)
    @Mapping(target="totalPrice", source ="totalPrice")
    @Mapping(target="basePrice", source="flight.price")
    @Mapping(target="externalFlight", source = "flight")
    @Mapping(target="bookingReference", source = "bookingReference")
    BookingEntity toEntity(CreateBookingRequest request, UserEntity user, DuffelFlightEntity flight, SeatEntity seat, BigDecimal totalPrice, String bookingReference);

    BookingResponse toResponse(BookingEntity entity);
}
