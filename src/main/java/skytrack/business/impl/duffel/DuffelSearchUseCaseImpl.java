package skytrack.business.impl.duffel;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import skytrack.business.mapper.DuffelFlightMapper;
import skytrack.business.useCase.flight.SearchDuffelFlightUseCase;
import skytrack.dto.duffel.DuffelFlightResponse;
import skytrack.dto.duffel.request.DuffelOfferRequest;
import skytrack.dto.duffel.request.DuffelPassenger;
import skytrack.dto.duffel.request.RequestData;
import skytrack.dto.duffel.request.RequestSlice;
import skytrack.dto.duffel.response.DuffelOfferResponse;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DuffelSearchUseCaseImpl implements SearchDuffelFlightUseCase {
    private final WebClient duffelClient;
    private final ObjectMapper mapper;

    @Override
    public Mono<List<DuffelFlightResponse>> searchFlights(String departureIata, String arrivalIata, LocalDate departureDate) {
        DuffelOfferRequest request = buildRequest(departureIata, arrivalIata, departureDate);
        return duffelClient.post().uri("/air/offer_requests?return_offers=true")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(raw -> {
                    try {
                        DuffelOfferResponse response = mapper.readValue(raw, DuffelOfferResponse.class);
                        return Mono.just(response);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                })
                .map(response -> response.getData().getOffers().stream()
                        .map(DuffelFlightMapper::toResponse)
                        .toList());
    }

    private DuffelOfferRequest buildRequest(String origin, String destination, LocalDate date){
        RequestSlice slice = RequestSlice.builder()
                .origin(origin)
                .destination(destination)
                .departureDate(date.toString())
                .build();

        RequestData data = RequestData.builder()
                .slices(List.of(slice))
                .passengers(List.of(DuffelPassenger.builder().type("adult").build()))
                .cabinClass("economy")
                .build();

        return DuffelOfferRequest.builder()
                .data(data)
                .build();
    }
}
