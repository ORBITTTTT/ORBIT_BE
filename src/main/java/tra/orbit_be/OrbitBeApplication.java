package tra.orbit_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // 엔티티 객체가 생성이 되거나 변경이 되었을 때 자동으로 값을 등록할 수 있습니다.
public class OrbitBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrbitBeApplication.class, args);
    }

}

