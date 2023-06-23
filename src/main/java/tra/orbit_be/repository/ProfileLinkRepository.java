package tra.orbit_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tra.orbit_be.model.user.ProfileLink;

import java.util.List;

public interface ProfileLinkRepository extends JpaRepository<ProfileLink, Long> {

    // user테이블의 userTableId로 찾기
    List<ProfileLink> findByUserUserTableId(Long userTableId);
}
