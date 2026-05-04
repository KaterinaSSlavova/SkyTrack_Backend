package skytrack.business.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookingReferenceGenerator {

    public String generate(){
        return "SKY-"+ UUID.randomUUID().toString()
                .replace("-","").substring(0,8).toUpperCase();
    }
}
