package skytrack.persistence.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import skytrack.business.repository.FlightRepository;
import skytrack.domain.entity.Flight;
import skytrack.persistence.entity.FlightEntity;
import skytrack.persistence.mapper.FlightMapper;
import skytrack.persistence.repo.JpaFlightRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FlightRepositoryImpl implements FlightRepository {
    private final JpaFlightRepository jpaFlightRepository;

    @Override
    public Flight saveFlight(Flight flight) {
        FlightEntity entity = FlightMapper.toEntity(flight);
        if(entity.getCreatedAt() == null) {
            entity.setCreatedAt(Instant.now());
        }
        FlightEntity savedEntity = jpaFlightRepository.save(entity);
        return FlightMapper.toDomain(savedEntity);
    }

    @Override
    public void updateFlight(Flight flight) {
        FlightEntity oldEntity = jpaFlightRepository.findById(flight.getId())
                .orElseThrow(() -> new IllegalStateException("Flight should exist before update!"));

        FlightEntity updatedEntity = FlightMapper.toEntity(flight);
        updatedEntity.setCreatedAt(oldEntity.getCreatedAt());
        jpaFlightRepository.save(updatedEntity);
    }

    @Override
    public Optional<Flight> findFlightById(Long flightId) {
       return jpaFlightRepository.findById(flightId)
               .map(FlightMapper::toDomain);
    }

    @Override
    public List<Flight> getAllFlights() {
        return jpaFlightRepository.findAll().stream().map(FlightMapper::toDomain).toList();
    }

    @Override
    public boolean existsById(Long flightId) {
        return jpaFlightRepository.existsById(flightId);
    }
}