package skytrack.business.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class TimeConverter {

    public Instant convertToUTC(LocalDateTime localTime, String timezone){
        ZoneId zone = ZoneId.of(timezone);
        return localTime.atZone(zone).toInstant();
    }

    public LocalDateTime convertToLocalTime(Instant timeUTC, String timezone){
        ZoneId zone = ZoneId.of(timezone);
        return LocalDateTime.ofInstant(timeUTC, zone);
    }
}
