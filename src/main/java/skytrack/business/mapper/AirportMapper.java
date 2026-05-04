package skytrack.business.mapper;

import org.mapstruct.Mapper;
import skytrack.dto.airport.AirportResponse;
import skytrack.dto.airport.CreateAirportRequest;
import skytrack.dto.airport.UpdateAirportRequest;
import skytrack.persistence.entity.AirportEntity;

@Mapper(componentModel = "spring")
public interface AirportMapper {
    AirportEntity toEntity(CreateAirportRequest request);

    AirportResponse toResponse(AirportEntity airport);

    AirportEntity toEntity(UpdateAirportRequest request);
}
