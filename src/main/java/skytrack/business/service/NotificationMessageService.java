package skytrack.business.service;

import org.springframework.stereotype.Service;
import skytrack.persistence.enumeration.NotificationType;

import java.time.LocalDateTime;

@Service
public class NotificationMessageService {

    public String getTitle(NotificationType type) {
        return switch(type){
            case FLIGHT_DELAYED ->  "Fight delayed";
            case FLIGHT_CANCELLED ->   "Fight cancelled";
            case GATE_CHANGED ->    "Gate changed";
        };
    }

    public String getMessage(NotificationType type, String flightNumber, String gate, LocalDateTime newDepartureTime) {
        return switch(type){
            case FLIGHT_DELAYED ->  "Your flight " +  flightNumber + " has been delayed. New departure time: " + newDepartureTime + ".";
            case FLIGHT_CANCELLED ->   "Your flight" + flightNumber + " has been cancelled. We apologize for the inconvenience. Please monitor the system for further updates regarding your flight.";
            case GATE_CHANGED ->     "Your flight " + flightNumber + " now departs from gate " + gate + ".";
        };
    }
}
