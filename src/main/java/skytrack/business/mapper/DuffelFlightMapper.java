package skytrack.business.mapper;

import skytrack.dto.duffel.DuffelFlightResponse;
import skytrack.dto.duffel.response.DuffelAirport;
import skytrack.dto.duffel.response.DuffelOffer;
import skytrack.dto.duffel.response.DuffelSegment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DuffelFlightMapper {
    public static DuffelFlightResponse toResponse(DuffelOffer offer){
        DuffelSegment segment = offer.getSlices().get(0).getSegments().get(0);
        DuffelAirport origin = segment.getOrigin();
        DuffelAirport destination = segment.getDestination();

        return DuffelFlightResponse.builder()
                .flightNumber(segment.getOperatingCarrierFlightNumber())
                .departureIataCode(origin.getIataCode())
                .arrivalIataCode(destination.getIataCode())
                .departureLocalTime(LocalDateTime.parse(segment.getDepartingAt()))
                .arrivalLocalTime(LocalDateTime.parse(segment.getArrivingAt()))
                .departureTimezone(origin.getTimeZone())
                .arrivalTimezone(destination.getTimeZone())
                .price(new BigDecimal(offer.getTotalAmount()))
                .currency(offer.getTotalCurrency())
                .build();
    }
}