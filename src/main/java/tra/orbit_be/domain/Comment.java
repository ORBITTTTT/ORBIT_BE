package tra.orbit_be.domain;

import lombok.*;
import tra.orbit_be.login.domain.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    // user 받아오기
    @ManyToOne
    @JoinColumn(name = "userTableId")
    private User user;
    
    // 프로젝트 받아오기
    
    // comment 내용
    @Column
    private String comment;
}
