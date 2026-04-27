package skytrack.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class PassengerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String passportNumber;
    private LocalDate dateOfBirth;
    private LocalDate passportExpiry;
    private String nationality;
}
