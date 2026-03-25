package skytrack.persistence.mapper;

import skytrack.domain.entity.Airport;
import skytrack.persistence.entity.AirportEntity;

public class AirportMapper {
    public static Airport toDomain(AirportEntity entity){
        return new Airport(
                entity.getId(),
                entity.getIataCode(),
                entity.getName(),
                entity.getCity(),
                entity.getCountry(),
                entity.getTimezone()
        );
    }

    public static AirportEntity toEntity(Airport airport){
        return new AirportEntity(
                airport.getId(),
                airport.getIataCode(),
                airport.getName(),
                airport.getCity(),
                airport.getCountry(),
                airport.getTimezone()
        );
    }
}