package tra.orbit_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tra.orbit_be.domain.Project;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByProjectId(Long projectId);  //Optional이란 'null일 수도 있는 객체'를 감싸는 일종의 Wrapper 클래스 =>null일 수도 있는 객체를 다룰 수 있도록 돕는다.
}
