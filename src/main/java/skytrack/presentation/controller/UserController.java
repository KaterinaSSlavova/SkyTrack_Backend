package skytrack.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import skytrack.business.useCase.user.DeleteUserUseCase;
import skytrack.business.useCase.user.GetLoggedUserCase;
import skytrack.business.useCase.user.UpdateUserUseCase;
import skytrack.dto.user.UpdateUserRequest;
import skytrack.dto.user.UserResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final GetLoggedUserCase getLoggedUserCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getLoggedUser(Authentication authentication) {
        UserResponse response = getLoggedUserCase.getLoggedUser(authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UpdateUserRequest request){
        UserResponse response = updateUserUseCase.updateUser(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUserAccount(){
        deleteUserUseCase.deleteUser();
        return ResponseEntity.noContent().build();
    }
}