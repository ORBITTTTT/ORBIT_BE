package tra.orbit_be.domain.user;

import lombok.*;
import tra.orbit_be.login.domain.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestTableId;

    // 기술 스택
    @Column
    private String stackName;

    @ManyToOne
    @JoinColumn(name = "userTableId")
    private User user;
}
