package skytrack.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import skytrack.persistence.entity.ExtrasEntity;

public interface ExtrasRepository extends JpaRepository<ExtrasEntity, Long> {
    ExtrasEntity findByName(String name);
}
