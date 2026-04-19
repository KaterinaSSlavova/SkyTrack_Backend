package skytrack.business.mapper;

import skytrack.dto.airport.AirportResponse;
import skytrack.dto.airport.CreateAirportRequest;
import skytrack.dto.airport.UpdateAirportRequest;
import skytrack.persistence.entity.AirportEntity;

public class AirportMapper {
    public static AirportEntity toEntity(CreateAirportRequest request){
        return AirportEntity.builder()
                .iataCode(request.getIataCode())
                .name(request.getName())
                .city(request.getCity())
                .country(request.getCountry())
                .timezone(request.getTimezone())
                .build();
    }

    public static AirportResponse toResponse(AirportEntity airport){
        return AirportResponse.builder()
                .id(airport.getId())
                .iataCode(airport.getIataCode())
                .name(airport.getName())
                .city(airport.getCity())
                .country(airport.getCountry())
                .timezone(airport.getTimezone())
                .build();
    }

    public static AirportEntity toEntity(UpdateAirportRequest request){
        return AirportEntity.builder()
                .id(request.getId())
                .iataCode(request.getIataCode())
                .name(request.getName())
                .city(request.getCity())
                .country(request.getCountry())
                .timezone(request.getTimezone())
                .build();
    }
}