package skytrack.domain.entity;

import lombok.Getter;

@Getter
public class FlightStatus {
    private Long id;
    private String name;

    public FlightStatus(Long id, String name){
        this.id = id;
        this.name = name;
    }
}