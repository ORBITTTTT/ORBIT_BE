package tra.orbit_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tra.orbit_be.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
