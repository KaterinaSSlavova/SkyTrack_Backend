package skytrack.business.mapper;

import skytrack.domain.entity.Airport;
import skytrack.dto.airport.AirportResponse;
import skytrack.dto.airport.CreateAirportRequest;
import skytrack.dto.airport.UpdateAirportRequest;

public class AirportMapper {
    public static Airport toDomain(CreateAirportRequest request){
        return Airport.builder()
                .iataCode(request.getIataCode())
                .name(request.getName())
                .city(request.getCity())
                .country(request.getCountry())
                .timezone(request.getTimezone())
                .build();
    }

    public static AirportResponse toResponse(Airport airport){
        return AirportResponse.builder()
                .id(airport.getId())
                .iataCode(airport.getIataCode())
                .name(airport.getName())
                .city(airport.getCity())
                .country(airport.getCountry())
                .timezone(airport.getTimezone())
                .build();
    }

    public static Airport toDomain(UpdateAirportRequest request){
        return Airport.builder()
                .id(request.getId())
                .iataCode(request.getIataCode())
                .name(request.getName())
                .city(request.getCity())
                .country(request.getCountry())
                .timezone(request.getTimezone())
                .build();
    }
}