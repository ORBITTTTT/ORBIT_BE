package tra.orbit_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tra.orbit_be.login.model.User;

public interface UserRepository extends JpaRepository<Long, User> {
}
