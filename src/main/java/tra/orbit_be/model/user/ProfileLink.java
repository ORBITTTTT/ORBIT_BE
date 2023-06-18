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
public class ProfileLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkTableId;

    // 링크 제목
    @Column
    private String profileLinkTitle;

    // 링크 주소
    @Column
    private String profileLink;

    @ManyToOne
    @JoinColumn(name = "user_userTableId")
    private User user;
}
