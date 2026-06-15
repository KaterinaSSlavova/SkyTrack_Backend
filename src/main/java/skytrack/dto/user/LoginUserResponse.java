package skytrack.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import skytrack.persistence.entity.RefreshToken;

@Getter
@AllArgsConstructor
@Builder
public class LoginUserResponse {
    private String refreshToken;
    private String token;
    private Long id;
    private String firstName;
    private String role;
}
