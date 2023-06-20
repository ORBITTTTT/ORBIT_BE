package tra.orbit_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tra.orbit_be.model.user.ProfileLink;

public interface ProfileLinkRepository extends JpaRepository<ProfileLink, Long> {
}
