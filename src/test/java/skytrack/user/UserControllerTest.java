package skytrack.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import skytrack.persistence.entity.RoleEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.enumeration.Role;
import skytrack.persistence.repo.RoleRepository;
import skytrack.persistence.repo.UserRepository;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void getLoggedUser_whenAuthenticated_shouldReturnUser() throws Exception {
        createPassenger();

        mockMvc.perform(get("/users/me")
                        .with(user("passenger@test.com").roles("PASSENGER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("passenger@test.com"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("Passenger"));
    }

    @Test
    void getLoggedUser_whenUserDoesNotExist_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/users/me")
                        .with(user("missing@test.com").roles("PASSENGER")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getLoggedUser_withoutAuthentication_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateUser_withoutAuthentication_shouldReturnUnauthorized() throws Exception {
        String body = """
                {
                  "firstName": "Updated",
                  "lastName": "Passenger",
                  "picture": "test-picture"
                }
                """;

        mockMvc.perform(patch("/users/me")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteUser_withoutAuthentication_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(delete("/users/me")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    private UserEntity createPassenger() {
        RoleEntity role = roleRepository.save(
                RoleEntity.builder()
                        .roleName(Role.PASSENGER)
                        .build()
        );

        return userRepository.save(
                UserEntity.builder()
                        .firstName("Test")
                        .lastName("Passenger")
                        .email("passenger@test.com")
                        .passwordHash("password")
                        .birthdate(LocalDate.of(2000, 1, 1))
                        .role(role)
                        .build()
        );
    }
}

