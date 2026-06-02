package skytrack.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import skytrack.business.service.JwtService;
import skytrack.business.useCase.notification.*;
import skytrack.dto.notification.NotificationResponse;
import skytrack.presentation.controller.NotificationController;
import skytrack.presentation.security.JwtAuthenticationFilter;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CacheManager cacheManager;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private GetNotificationUseCase getNotificationUseCase;

    @MockitoBean
    private GetAllNotificationsUseCase getAllNotificationsUseCase;

    @MockitoBean
    private MarkAllNotificationsAsReadUseCase markAllNotificationsAsReadUseCase;

    @MockitoBean
    private MarkNotificationAsReadUseCase markNotificationAsReadUseCase;

    @MockitoBean
    private GetUnreadNotificationsUseCase getUnreadNotificationsUseCase;

    @BeforeEach
    void setupFilter() throws Exception {
        doAnswer(invocation -> {
            jakarta.servlet.FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

    @Test
    @WithMockUser
    void getAllNotifications_shouldReturn200() throws Exception {
        NotificationResponse notification = NotificationResponse.builder()
                .id(1L)
                .message("Your flight AMS-JFK has been updated.")
                .read(false)
                .build();

        when(getAllNotificationsUseCase.getAllNotifications()).thenReturn(List.of(notification));

        mockMvc.perform(get("/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].message").value("Your flight AMS-JFK has been updated."))
                .andExpect(jsonPath("$[0].read").value(false));

        verify(getAllNotificationsUseCase).getAllNotifications();
    }

    @Test
    @WithMockUser
    void getAllNotifications_empty_shouldReturn200WithEmptyList() throws Exception {
        when(getAllNotificationsUseCase.getAllNotifications()).thenReturn(List.of());

        mockMvc.perform(get("/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(getAllNotificationsUseCase).getAllNotifications();
    }

    @Test
    @WithMockUser
    void getUnreadNotifications_shouldReturn200() throws Exception {
        NotificationResponse notification = NotificationResponse.builder()
                .id(2L)
                .message("Gate changed to B12.")
                .read(false)
                .build();

        when(getUnreadNotificationsUseCase.getUnreadNotifications()).thenReturn(List.of(notification));

        mockMvc.perform(get("/notifications/unread"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].read").value(false));

        verify(getUnreadNotificationsUseCase).getUnreadNotifications();
    }

    @Test
    @WithMockUser
    void getUnreadNotifications_empty_shouldReturn200WithEmptyList() throws Exception {
        when(getUnreadNotificationsUseCase.getUnreadNotifications()).thenReturn(List.of());

        mockMvc.perform(get("/notifications/unread"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(getUnreadNotificationsUseCase).getUnreadNotifications();
    }

    @Test
    @WithMockUser
    void getNotificationById_shouldReturn200() throws Exception {
        NotificationResponse notification = NotificationResponse.builder()
                .id(1L)
                .message("Boarding now at gate A3.")
                .read(true)
                .build();

        when(getNotificationUseCase.getNotification(1L)).thenReturn(notification);

        mockMvc.perform(get("/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.message").value("Boarding now at gate A3."))
                .andExpect(jsonPath("$.read").value(true));

        verify(getNotificationUseCase).getNotification(1L);
    }

    @Test
    @WithMockUser
    void markAsRead_shouldReturn204() throws Exception {
        mockMvc.perform(patch("/notifications/1/read"))
                .andExpect(status().isNoContent());

        verify(markNotificationAsReadUseCase).markNotificationAsRead(1L);
    }

    @Test
    @WithMockUser
    void markAllAsRead_shouldReturn204() throws Exception {
        mockMvc.perform(patch("/notifications/read-all"))
                .andExpect(status().isNoContent());

        verify(markAllNotificationsAsReadUseCase).markAllNotificationsAsRead();
    }
}