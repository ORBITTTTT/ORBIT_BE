package tra.orbit_be.model.user;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stackTableId;

    // 기술 스택
    @Column
    @OneToMany(mappedBy = "stack")
    private List<InterestStack> stacks;
}
