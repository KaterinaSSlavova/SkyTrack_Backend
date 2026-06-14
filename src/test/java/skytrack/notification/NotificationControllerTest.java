package skytrack.notification;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import skytrack.persistence.entity.RoleEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.enumeration.Role;
import skytrack.persistence.repo.RoleRepository;
import skytrack.persistence.repo.UserRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void getAllNotifications_withPassenger_shouldReturnOk() throws Exception {
        createPassenger();

        mockMvc.perform(get("/notifications")
                        .with(user("passenger@test.com").roles("PASSENGER")))
                .andExpect(status().isOk());
    }

    @Test
    void getAllNotifications_withAdmin_shouldReturnForbidden() throws Exception {
        createAdmin();

        mockMvc.perform(get("/notifications")
                        .with(user("admin@test.com").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUnreadNotifications_withPassenger_shouldReturnOk() throws Exception {
        createPassenger();

        mockMvc.perform(get("/notifications/unread")
                        .with(user("passenger@test.com").roles("PASSENGER")))
                .andExpect(status().isOk());
    }

    @Test
    void getUnreadNotifications_withAdmin_shouldReturnForbidden() throws Exception {
        createAdmin();

        mockMvc.perform(get("/notifications/unread")
                        .with(user("admin@test.com").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getNotification_whenNotificationDoesNotExist_shouldReturnNotFound() throws Exception {
        createPassenger();

        mockMvc.perform(get("/notifications/666666666")
                        .with(user("passenger@test.com").roles("PASSENGER")))
                .andExpect(status().isNotFound());
    }

    @Test
    void markAsRead_withAdmin_shouldReturnForbidden() throws Exception {
        createAdmin();

        mockMvc.perform(patch("/notifications/1/read")
                        .with(user("admin@test.com").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void markAllAsRead_withPassenger_shouldReturnNoContent() throws Exception {
        createPassenger();

        mockMvc.perform(patch("/notifications/read-all")
                        .with(user("passenger@test.com").roles("PASSENGER"))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void markAllAsRead_withAdmin_shouldReturnForbidden() throws Exception {
        createAdmin();

        mockMvc.perform(patch("/notifications/read-all")
                        .with(user("admin@test.com").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isForbidden());
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
                        .role(role)
                        .build()
        );
    }

    private UserEntity createAdmin() {
        RoleEntity role = roleRepository.save(
                RoleEntity.builder()
                        .roleName(Role.ADMIN)
                        .build()
        );

        return userRepository.save(
                UserEntity.builder()
                        .firstName("Test")
                        .lastName("Admin")
                        .email("admin@test.com")
                        .passwordHash("password")
                        .role(role)
                        .build()
        );
    }
}
