package skytrack.domain.entity;

import lombok.Builder;
import lombok.Getter;
import skytrack.domain.exception.InvalidAirportException;


@Getter
public class Airport {
    private Long id;
    private String iataCode;
    private String name;
    private String city;
    private String country;
    private String timezone;

    @Builder
    public Airport(Long id, String iataCode, String name, String city, String country, String timezone) {
        validateIATACode(iataCode);
        validateName(name);
        validateCity(city);
        validateCountry(country);
        validateTimeZone(timezone);

        this.id = id;
        this.iataCode = iataCode;
        this.name = name;
        this.city = city;
        this.country = country;
        this.timezone = timezone;
    }

    private void validateIATACode(String iataCode) {
        if(iataCode == null || iataCode.length() != 3){
            throw new InvalidAirportException(InvalidAirportException.INVALID_IATA);
        }
    }

    private void validateName(String name){
        if(name == null || name.isBlank()){
            throw new InvalidAirportException(InvalidAirportException.INVALID_NAME);
        }
    }

    private void validateCountry(String country){
        if(country == null || country.isBlank()){
            throw new InvalidAirportException(InvalidAirportException.INVALID_COUNTRY);
        }
    }

    private void validateCity(String city){
        if(city == null || city.isBlank()){
            throw new InvalidAirportException(InvalidAirportException.INVALID_CITY);
        }
    }

    private void validateTimeZone(String timezone){
        if(timezone == null || timezone.isBlank()){
            throw new InvalidAirportException(InvalidAirportException.INVALID_TIMEZONE);
        }
    }
}
