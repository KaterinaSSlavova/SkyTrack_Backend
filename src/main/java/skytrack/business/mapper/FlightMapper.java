package skytrack.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import skytrack.dto.duffel.DuffelFlightResponse;
import skytrack.dto.duffel.SavedFlightResponse;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.entity.DuffelFlightEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring", uses = AirportMapper.class)
public interface FlightMapper {

    @Mapping(target="departureLocalTime", source = "departureLocalTime")
    @Mapping(target="arrivalLocalTime", source = "arrivalLocalTime")
    SavedFlightResponse toResponse(DuffelFlightEntity entity, LocalDateTime departureLocalTime, LocalDateTime arrivalLocalTime);

    default SavedFlightResponse toResponse(DuffelFlightEntity entity) {
        LocalDateTime departure = entity.getDepartureTime() != null && entity.getDepartureTimezone() != null
                ? LocalDateTime.ofInstant(entity.getDepartureTime(), ZoneId.of(entity.getDepartureTimezone()))
                : null;

        LocalDateTime arrival = entity.getArrivalTime() != null && entity.getArrivalTimezone() != null
                ? LocalDateTime.ofInstant(entity.getArrivalTime(), ZoneId.of(entity.getArrivalTimezone()))
                : null;

        return toResponse(entity, departure, arrival);
    }

    @Mapping(target="id", ignore = true)
    @Mapping(target="departureTime", source="departureAt")
    @Mapping(target="arrivalTime", source="arrivalAt")
    DuffelFlightEntity toEntity(DuffelFlightResponse response, Instant departureAt, Instant arrivalAt, AirportEntity departureAirport, AirportEntity arrivalAirport);
}
