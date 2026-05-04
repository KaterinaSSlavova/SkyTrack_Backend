package skytrack.business.mapper;

import skytrack.dto.flight.CreateFlightRequest;
import skytrack.dto.flight.FlightResponse;
import skytrack.dto.flight.UpdateFlightRequest;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.entity.FlightEntity;
import skytrack.persistence.entity.FlightStatusEntity;

import java.time.Instant;
import java.time.LocalDateTime;

public class InternalFlightMapper {
    public static FlightEntity toEntity (CreateFlightRequest request,
                                         AirportEntity depAirport,
                                         AirportEntity arrAirport,
                                         Instant depTimeUTC,
                                         Instant arrTimeUTC,
                                         FlightStatusEntity status){
        return new FlightEntity(
                null,
                request.getFlightNumber(),
                depAirport,
                arrAirport,
                depTimeUTC,
                arrTimeUTC,
                request.getGate(),
                request.getTerminal(),
                request.getCapacity(),
                request.getPrice(),
                status,
                Instant.now()
                );
    }

    public static FlightResponse toResponse(FlightEntity flight, LocalDateTime depLocalTime, LocalDateTime arrLocalTime) {
        return new FlightResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getDepartureAirport().getIataCode(),
                flight.getArrivalAirport().getIataCode(),
                depLocalTime,
                arrLocalTime,
                flight.getDepartureAirport().getTimezone(),
                flight.getArrivalAirport().getTimezone(),
                flight.getGate(),
                flight.getTerminal(),
                flight.getPrice(),
                flight.getCapacity(),
                flight.getStatus().getName()
        );
    }

    public static void updateEntity(FlightEntity entity,
                                    UpdateFlightRequest request,
                                    AirportEntity depAirport,
                                    AirportEntity arrAirport,
                                    Instant depTimeUTC,
                                    Instant arrTimeUTC,
                                    FlightStatusEntity status) {
        entity.setId(entity.getId());
        entity.setFlightNumber(request.getFlightNumber());
        entity.setDepartureAirport(depAirport);
        entity.setArrivalAirport(arrAirport);
        entity.setDepartureTimeUTC(depTimeUTC);
        entity.setArrivalTimeUTC(arrTimeUTC);
        entity.setGate(request.getGate());
        entity.setTerminal(request.getTerminal());
        entity.setCapacity(request.getCapacity());
        entity.setPrice(request.getPrice());
        entity.setStatus(status);
    }
}
