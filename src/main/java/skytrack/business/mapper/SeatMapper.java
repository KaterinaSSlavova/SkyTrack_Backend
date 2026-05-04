package skytrack.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import skytrack.dto.seat.SeatResponse;
import skytrack.persistence.entity.SeatEntity;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    SeatEntity toEntity(SeatResponse response);

    @Mapping(target="available", source="isAvailable")
    SeatResponse toResponse(SeatEntity entity, Boolean isAvailable);
}
