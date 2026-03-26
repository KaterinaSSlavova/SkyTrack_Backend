package skytrack.persistence.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import skytrack.business.repository.AirportRepository;
import skytrack.domain.entity.Airport;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.mapper.AirportMapper;
import skytrack.persistence.repo.JpaAirportRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AirportRepositoryImpl implements AirportRepository {
    private final JpaAirportRepository jpaAirportRepository;

    @Override
    public Airport saveAirport(Airport airport) {
        AirportEntity entity = AirportMapper.toEntity(airport);
        entity.setIsArchived(false);
        AirportEntity savedEntity = jpaAirportRepository.save(entity);
        return AirportMapper.toDomain(savedEntity);
    }

    @Override
    public void updateAirport(Airport airport) {
        AirportEntity entity = AirportMapper.toEntity(airport);
        jpaAirportRepository.save(entity);
    }

    @Override
    public Optional<Airport> getAirportById(Long airportId) {
        return jpaAirportRepository.findByIdAndIsArchivedFalse(airportId)
                .map(AirportMapper::toDomain);
    }

    @Override
    public void deleteAirport(Long airportId) {
        AirportEntity entity = jpaAirportRepository.findByIdAndIsArchivedFalse(airportId)
                .orElseThrow(() -> new IllegalStateException("Airport must exist before deleting it!"));
        entity.setIsArchived(true);
        jpaAirportRepository.save(entity);
    }

    @Override
    public List<Airport> getAllAirports() {
        return jpaAirportRepository
                .findByIsArchivedFalse().stream().map(AirportMapper::toDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaAirportRepository.existsById(id);
    }
}