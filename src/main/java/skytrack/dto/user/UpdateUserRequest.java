package skytrack.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    private String picture;
    private String firstName;
    private String lastName;

    @Email
    private String email;
    private LocalDate birthDate;
}
