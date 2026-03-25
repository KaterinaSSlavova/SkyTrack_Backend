package skytrack.persistence.mapper;

import skytrack.domain.entity.FlightStatus;
import skytrack.persistence.entity.FlightStatusEntity;

public class FlightStatusMapper {
    public static FlightStatus toDomain(FlightStatusEntity entity) {
        return new FlightStatus(
                entity.getId(),
                entity.getName()
        );
    }

    public static FlightStatusEntity toEntity(FlightStatus status){
        return new FlightStatusEntity(
                status.getId(),
                status.getName()
        );
    }
}
