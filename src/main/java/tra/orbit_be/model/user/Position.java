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
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long posTableId;

    // 직군
    @Column
    private String posName;

    @ManyToOne
    @JoinColumn(name = "user_userTableId")
    private User user;
}
