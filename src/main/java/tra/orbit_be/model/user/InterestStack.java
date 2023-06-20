package tra.orbit_be.model.user;

import lombok.*;
import tra.orbit_be.login.model.User;

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
    @Column(unique = true)
    private String stackName;

    @ManyToOne
    @JoinColumn(name = "user_userTableId")
    private User user;
}
