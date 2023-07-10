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
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long posTableId;

    // 직군
    @Column
    private String posName;

    @ManyToOne
    @JoinColumn(name = "userTableId")
    private User user;
}
