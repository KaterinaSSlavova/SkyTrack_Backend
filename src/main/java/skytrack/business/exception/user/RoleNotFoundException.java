package skytrack.business.exception.user;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String role) {
        super("The role:  " + role + " was not found!");
    }
}
