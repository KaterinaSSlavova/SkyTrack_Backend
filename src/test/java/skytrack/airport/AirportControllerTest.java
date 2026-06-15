package skytrack.airport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.repo.AirportRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AirportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AirportRepository airportRepository;

    @Test
    void createAirport_shouldReturnCreated() throws Exception{
        String body= """
                {
                   "iataCode": "TST",
                    "name": "Test Airport",
                    "city": "Test City",
                    "country": "Test Country",
                    "timezone": "Europe/Amsterdam"
                }
                """;

        mockMvc.perform(post("/airports")
                        .with(user("admin@test.com").roles("ADMIN"))
                        .with(csrf()).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.iataCode").value("TST"))
                .andExpect(jsonPath("$.name").value("Test Airport"))
                .andExpect(jsonPath("$.city").value("Test City"))
                .andExpect(jsonPath("$.country").value("Test Country"))
                .andExpect(jsonPath("$.timezone").value("Europe/Amsterdam"));
    }

    @Test
    void createAirport_withPassenger_shouldReturnForbidden() throws Exception {
        String body= """
                {
                   "iataCode": "TST",
                    "name": "Test Airport",
                    "city": "Test City",
                    "country": "Test Country",
                    "timezone": "Europe/Amsterdam"
                }
                """;

        mockMvc.perform(post("/airports")
                .with(user("passenger@test.com").roles("PASSENGER"))
                .with(csrf()).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    void createAirport_withInvalidData_shouldReturnBadRequest() throws Exception{
        String body= """
                {
                   "iataCode": "",
                    "name": "",
                    "city": "",
                    "country": "",
                    "timezone": ""
                }
                """;
        mockMvc.perform(post("/airports").with(user("admin@test.com").roles("ADMIN"))
                .with(csrf()).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllAirports_withPassenger_shouldReturnOk() throws Exception{
         mockMvc.perform(get("/airports").with(user("passenger@test.com").roles("PASSENGER"))
                 .with(csrf())).andExpect(status().isOk());
    }

    @Test
    void getAllAirports_withAdmin_shouldReturnOk() throws Exception{
        mockMvc.perform(get("/airports").with(user("admin@test.com").roles("ADMIN"))
                .with(csrf())).andExpect(status().isOk());
    }

    @Test
    void getAirportById_whenAirportExists_shouldReturnAirport() throws Exception{
        AirportEntity airport =  airportRepository.save(createAirport());

        mockMvc.perform(get("/airports/"+ airport.getId())
                .with(user("passenger@test.com").roles("PASSENGER"))
                .with(csrf())).andExpect(status().isOk())
                .andExpect(jsonPath("$.iataCode").value("TST"))
                .andExpect(jsonPath("$.name").value("Test Airport"))
                .andExpect(jsonPath("$.city").value("Test City"))
                .andExpect(jsonPath("$.country").value("Test Country"))
                .andExpect(jsonPath("$.timezone").value("Europe/Amsterdam"));;
    }

    @Test
    void getAirportById_whenIdDoesNotExist_shouldReturnNotFound() throws Exception{
        mockMvc.perform(get("/airports/666666666")
                .with(user("passenger@test.com").roles("PASSENGER"))
                .with(csrf())).andExpect(status().isNotFound());
    }

    @Test
    void updateAirport_withAdmin_shouldReturnNoContent() throws Exception {
        AirportEntity airport = airportRepository.save(createAirport());

        String body = """
            {
              "iataCode": "UPD",
              "name": "Updated Airport",
              "city": "Updated City",
              "country": "Updated Country",
              "timezone": "Europe/Amsterdam"
            }
            """;

        mockMvc.perform(put("/airports/" + airport.getId())
                        .with(user("admin@test.com").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateAirport_withPassenger_shouldReturnForbidden() throws Exception {
        AirportEntity airport = airportRepository.save(createAirport());

        String body = """
            {
              "iataCode": "UPD",
              "name": "Updated Airport",
              "city": "Updated City",
              "country": "Updated Country",
              "timezone": "Europe/Amsterdam"
            }
            """;

        mockMvc.perform(put("/airports/" + airport.getId())
                        .with(user("passenger@test.com").roles("PASSENGER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    void archiveAirport_withAdmin_shouldReturnNoContent() throws Exception {
        AirportEntity airport = airportRepository.save(createAirport());

        mockMvc.perform(patch("/airports/" + airport.getId())
                        .with(user("admin@test.com").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void archiveAirport_withPassenger_shouldReturnForbidden() throws Exception {
        AirportEntity airport = airportRepository.save(createAirport());

        mockMvc.perform(patch("/airports/" + airport.getId())
                        .with(user("passenger@test.com").roles("PASSENGER"))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void searchAirports_withPassenger_shouldReturnMatchingAirports() throws Exception {
        airportRepository.save(createAirport());

        mockMvc.perform(get("/airports/search")
                        .param("input", "Test")
                        .with(user("passenger@test.com").roles("PASSENGER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].iataCode").value("TST"));
    }

    @Test
    void searchAirports_withAdmin_shouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/airports/search")
                        .param("input", "Test")
                        .with(user("admin@test.com").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }

    private AirportEntity createAirport() {
        return AirportEntity.builder()
                .iataCode("TST")
                .name("Test Airport")
                .city("Test City")
                .country("Test Country")
                .timezone("Europe/Amsterdam")
                .build();
    }
}
