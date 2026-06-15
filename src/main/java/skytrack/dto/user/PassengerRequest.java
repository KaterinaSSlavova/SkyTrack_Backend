package skytrack.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerRequest {

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @Email
    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank(message = "Gender is required.")
    private String gender;

    @NotBlank(message = "Passport number is required.")
    private String passportNumber;

    @NotNull(message = "Please select the date of birth of the passenger.")
    private LocalDate dateOfBirth;

    @NotNull(message = "Please enter passport expiration.")
    private LocalDate passportExpiry;

    @NotBlank(message = "Please enter the nationality of the passenger.")
    private String nationality;
}
