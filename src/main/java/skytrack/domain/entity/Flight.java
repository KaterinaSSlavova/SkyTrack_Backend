package skytrack.domain.entity;

import lombok.Builder;
import lombok.Getter;
import skytrack.domain.exception.InvalidFlightException;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class Flight {
    private Long id;
    private String flightNumber;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private Instant departureTimeUTC;
    private Instant arrivalTimeUTC;
    private String gate;
    private String terminal;
    private Integer capacity;
    private BigDecimal price;
    private FlightStatus status;

    public Flight(Long id, String flightNumber, Airport departureAirport,
                  Airport arrivalAirport, Instant departureTimeUTC, Instant arrivalTimeUTC,
                  String gate, String terminal, Integer capacity, BigDecimal price,
                  FlightStatus status) {
        validateFlightNumber(flightNumber);
        validateAirport(departureAirport, arrivalAirport);
        validateCapacity(capacity);
        validateSchedule(departureTimeUTC, arrivalTimeUTC);
        validatePrice(price);
        validateStatus(status);

        this.id = id;
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTimeUTC = departureTimeUTC;
        this.arrivalTimeUTC = arrivalTimeUTC;
        this.gate = gate;
        this.terminal = terminal;
        this.capacity = capacity;
        this.price = price;
        this.status = status;
    }

    private void validateFlightNumber(String flightNumber) {
        if (flightNumber == null || flightNumber.isBlank()) {
            throw new InvalidFlightException(InvalidFlightException.FLIGHT_NUM_NULL);
        }
    }

    private void validateAirport(Airport departureAirport, Airport arrivalAirport) {
        if(departureAirport == null || arrivalAirport == null){
            throw new InvalidFlightException(InvalidFlightException.AIRPORT_NULL);
        }

        if(departureAirport.equals(arrivalAirport)){
            throw new InvalidFlightException(InvalidFlightException.SAME_AIRPORT);
        }
    }

    private void validateCapacity(Integer capacity) {
        if (capacity == null || capacity <= 0) {
            throw new InvalidFlightException(InvalidFlightException.INVALID_CAPACITY);
        }
    }

    private void validateSchedule(Instant departureTimeUTC, Instant arrivalTimeUTC) {
        if(departureTimeUTC == null || arrivalTimeUTC == null){
            throw new InvalidFlightException(InvalidFlightException.SCHEDULE_UNDEFINED);
        }

        if(arrivalTimeUTC.isBefore(departureTimeUTC)){
            throw new InvalidFlightException(InvalidFlightException.INVALID_SCHEDULE);
        }
    }

    private void validatePrice(BigDecimal price){
        if(price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidFlightException(InvalidFlightException.INVALID_PRICE);
        }
    }

    private void validateStatus(FlightStatus status) {
        if(status == null || status.getName()==null || status.getName().isBlank()){
            throw new InvalidFlightException(InvalidFlightException.STATUS_NULL);
        }
    }
}
