package tra.orbit_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tra.orbit_be.model.user.Position;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {

    // user테이블의 userTableId로 찾기
    List<Position> findByUserUserTableId(Long userTableId);
}
