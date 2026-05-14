package skytrack.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import skytrack.dto.notification.NotificationResponse;
import skytrack.persistence.entity.NotificationEntity;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target="flightId", source="flight.id")
    @Mapping(target="flightNumber", source="flight.flightNumber")
    NotificationResponse toResponse(NotificationEntity notification);
}
