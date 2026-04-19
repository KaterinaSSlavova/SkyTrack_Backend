package skytrack.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String picture;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private LocalDate birthDate;
    private String role;
}
