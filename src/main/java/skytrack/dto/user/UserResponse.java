package skytrack.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
    private String role;
}
