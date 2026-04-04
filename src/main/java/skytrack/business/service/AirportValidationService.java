package skytrack.business.service;

import org.springframework.stereotype.Service;
import skytrack.business.exception.InvalidAirportException;
import skytrack.persistence.entity.AirportEntity;

@Service
public class AirportValidationService {
    public void validateAirport(AirportEntity airport){
        if(airport.getIataCode() == null || airport.getIataCode().length() != 3){
            throw new InvalidAirportException(InvalidAirportException.INVALID_IATA);
        }

        if(airport.getName() == null || airport.getName().isBlank()){
            throw new InvalidAirportException(InvalidAirportException.INVALID_NAME);
        }

        if(airport.getCountry() == null || airport.getCountry().isBlank()){
            throw new InvalidAirportException(InvalidAirportException.INVALID_COUNTRY);
        }

        if(airport.getCity() == null || airport.getCity().isBlank()){
            throw new InvalidAirportException(InvalidAirportException.INVALID_CITY);
        }

        if(airport.getTimezone() == null || airport.getTimezone().isBlank()){
            throw new InvalidAirportException(InvalidAirportException.INVALID_TIMEZONE);
        }
    }
}
