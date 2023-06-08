package tra.orbit_be.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tra.orbit_be.login.model.User;

import java.util.Optional;

public interface OAuthRepository extends JpaRepository<User, Long> {

    // username(로그인 id) 존재 여부
    Optional<User> findByUserEmail(String username);

    // 닉네임 중복 검사 할 때 사용
    Optional<User> findByUserNickname(String nickname);

    // socialId로 같은 회원이 있는지 찾는다.
    Optional<User> findBySocialId(String socialId);

}
