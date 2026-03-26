package skytrack.persistence.mapper;

import skytrack.domain.entity.Flight;
import skytrack.persistence.entity.FlightEntity;

public class FlightMapper {
   public static Flight toDomain(FlightEntity entity){
       return new Flight(
               entity.getId(),
               entity.getFlightNumber(),
               AirportMapper.toDomain(entity.getDepartureAirport()),
               AirportMapper.toDomain(entity.getArrivalAirport()),
               entity.getDepartureTimeUTC(),
               entity.getArrivalTimeUTC(),
               entity.getGate(),
               entity.getTerminal(),
               entity.getCapacity(),
               entity.getPrice(),
               FlightStatusMapper.toDomain(entity.getStatus())
       );
   }

   public static FlightEntity toEntity(Flight flight){
       return new FlightEntity(
               flight.getId(),
               flight.getFlightNumber(),
               AirportMapper.toEntity(flight.getDepartureAirport()),
               AirportMapper.toEntity(flight.getArrivalAirport()),
               flight.getDepartureTimeUTC(),
               flight.getArrivalTimeUTC(),
               flight.getGate(),
               flight.getTerminal(),
               flight.getCapacity(),
               flight.getPrice(),
               FlightStatusMapper.toEntity(flight.getStatus()),
               null,
               null
       );
   }
}
