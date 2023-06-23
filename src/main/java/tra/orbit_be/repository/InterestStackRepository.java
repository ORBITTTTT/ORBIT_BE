package tra.orbit_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tra.orbit_be.model.user.InterestStack;

import java.util.List;

public interface InterestStackRepository extends JpaRepository<InterestStack, Long> {

    // user테이블의 userTableId로 찾기
    List<InterestStack> findByUserUserTableId(Long userTableId);
}
