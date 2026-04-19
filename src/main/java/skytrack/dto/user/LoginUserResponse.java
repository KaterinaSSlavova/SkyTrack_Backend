package skytrack.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUserResponse {
    private String token;
    private Long id;
    private String firstName;
    private String role;
}
