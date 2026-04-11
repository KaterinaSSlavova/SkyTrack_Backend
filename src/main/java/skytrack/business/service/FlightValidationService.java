package skytrack.business.service;

import org.springframework.stereotype.Service;
import skytrack.business.exception.flight.InvalidFlightException;
import skytrack.persistence.entity.FlightEntity;

import java.math.BigDecimal;

@Service
public class FlightValidationService {
    public void validateFlight(FlightEntity flight){
        if (flight.getFlightNumber() == null || flight.getFlightNumber().isBlank()) {
            throw new InvalidFlightException(InvalidFlightException.FLIGHT_NUM_NULL);
        }

        if(flight.getDepartureAirport() == null || flight.getArrivalAirport() == null){
            throw new InvalidFlightException(InvalidFlightException.AIRPORT_NULL);
        }

        if(flight.getDepartureAirport().equals(flight.getArrivalAirport())){
            throw new InvalidFlightException(InvalidFlightException.SAME_AIRPORT);
        }

        if (flight.getCapacity() == null || flight.getCapacity() <= 0) {
            throw new InvalidFlightException(InvalidFlightException.INVALID_CAPACITY);
        }

        if(flight.getDepartureTimeUTC() == null || flight.getArrivalTimeUTC() == null){
            throw new InvalidFlightException(InvalidFlightException.SCHEDULE_UNDEFINED);
        }

        if(flight.getArrivalTimeUTC().isBefore(flight.getDepartureTimeUTC())){
            throw new InvalidFlightException(InvalidFlightException.INVALID_SCHEDULE);
        }

        if(flight.getPrice() == null || flight.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidFlightException(InvalidFlightException.INVALID_PRICE);
        }

        if(flight.getStatus() == null || flight.getStatus().getName()==null || flight.getStatus().getName().isBlank()){
            throw new InvalidFlightException(InvalidFlightException.STATUS_NULL);
        }
    }

}
