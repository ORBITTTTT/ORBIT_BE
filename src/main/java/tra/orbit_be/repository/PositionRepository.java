package tra.orbit_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tra.orbit_be.model.user.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {
}
