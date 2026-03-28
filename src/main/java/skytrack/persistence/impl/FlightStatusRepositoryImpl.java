package skytrack.persistence.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import skytrack.business.repository.FlightStatusRepository;
import skytrack.domain.entity.FlightStatus;
import skytrack.persistence.entity.FlightStatusEntity;
import skytrack.business.exception.FlightStatusNotFoundException;
import skytrack.persistence.mapper.FlightStatusMapper;
import skytrack.persistence.repo.JpaFlightStatusRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FlightStatusRepositoryImpl implements FlightStatusRepository {
    private final JpaFlightStatusRepository jpaFlightStatusRepository;

    @Override
    public FlightStatus saveFlightStatus(FlightStatus flightStatus) {
        FlightStatusEntity entity = FlightStatusMapper.toEntity(flightStatus);
        return FlightStatusMapper.toDomain(jpaFlightStatusRepository.save(entity));
    }

    @Override
    public Optional<FlightStatus> getFlightStatusById(Long statusId) {
        return jpaFlightStatusRepository
                .findById(statusId).map(FlightStatusMapper::toDomain);
    }

    @Override
    public void deleteFlightStatus(Long statusId) {
        if(!existsById(statusId)){
            throw new FlightStatusNotFoundException(statusId);
        }
        jpaFlightStatusRepository.deleteById(statusId);
    }

    @Override
    public List<FlightStatus> getAllFlightStatuses() {
        return jpaFlightStatusRepository.findAll()
                .stream().map(FlightStatusMapper::toDomain).toList();
    }

    @Override
    public Optional<FlightStatus> findFlightStatusByName(String flightStatusName){
        return jpaFlightStatusRepository.findFlightStatusByName(flightStatusName)
                .map(FlightStatusMapper::toDomain);
    }

    @Override
    public boolean existsById(Long statusId) {
        return jpaFlightStatusRepository.existsById(statusId);
    }
}