package skytrack.business.mapper;

import skytrack.dto.duffel.DuffelFlightResponse;
import skytrack.dto.duffel.response.DuffelAirport;
import skytrack.dto.duffel.response.DuffelOffer;
import skytrack.dto.duffel.response.DuffelSegment;
import skytrack.dto.duffel.response.DuffelSlices;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.entity.DuffelFlightEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

public class DuffelFlightMapper {
    public static DuffelFlightResponse toResponse(DuffelOffer offer){
        DuffelSlices slice = offer.getSlices().get(0);
        DuffelSegment firstSegment = slice.getSegments().get(0);
        DuffelSegment lastSegment = slice.getSegments().get(slice.getSegments().size() - 1);

        DuffelAirport origin = firstSegment.getOrigin();
        DuffelAirport destination = lastSegment.getDestination();

        return DuffelFlightResponse.builder()
                .externalId(offer.getId().split(":")[0])
                .flightNumber(firstSegment.getOperatingCarrierFlightNumber())
                .departureIataCode(origin.getIataCode())
                .arrivalIataCode(destination.getIataCode())
                .departureLocalTime(LocalDateTime.parse(firstSegment.getDepartingAt()))
                .arrivalLocalTime(LocalDateTime.parse(lastSegment.getArrivingAt()))
                .departureTimezone(origin.getTimeZone())
                .arrivalTimezone(destination.getTimeZone())
                .price(new BigDecimal(offer.getTotalAmount()))
                .currency(offer.getTotalCurrency())
                .build();
    }
}