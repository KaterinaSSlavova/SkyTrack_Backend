package skytrack.business.mapper;

import skytrack.domain.entity.Airport;
import skytrack.domain.entity.Flight;
import skytrack.domain.entity.FlightStatus;
import skytrack.dto.flight.CreateFlightRequest;
import skytrack.dto.flight.FlightResponse;
import skytrack.dto.flight.UpdateFlightRequest;

import java.time.Instant;
import java.time.LocalDateTime;

public class FlightMapper {
    public static Flight toDomain (CreateFlightRequest request,
                                    Airport depAirport,
                                    Airport arrAirport,
                                    Instant depTimeUTC,
                                    Instant arrTimeUTC,
                                    FlightStatus status){
        return new Flight(null,
                request.getFlightNumber(),
                depAirport,
                arrAirport,
                depTimeUTC,
                arrTimeUTC,
                request.getGate(),
                request.getTerminal(),
                request.getCapacity(),
                request.getPrice(),
                status
                );
    }

    public static FlightResponse toResponse(Flight flight, LocalDateTime depLocalTime, LocalDateTime arrLocalTime) {
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

    public static Flight toDomain (UpdateFlightRequest request,
                                   Airport depAirport,
                                   Airport arrAirport,
                                   Instant depTimeUTC,
                                   Instant arrTimeUTC,
                                   FlightStatus status){
        return new Flight(
                request.getId(),
                request.getFlightNumber(),
                depAirport,
                arrAirport,
                depTimeUTC,
                arrTimeUTC,
                request.getGate(),
                request.getTerminal(),
                request.getCapacity(),
                request.getPrice(),
                status
        );
    }
}
