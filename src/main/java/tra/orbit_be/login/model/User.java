package tra.orbit_be.login.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import tra.orbit_be.login.enums.SocialType;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userTableId;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String userNickname;

    @Column
    private String userProfileImage;

    @Column
    private int userLevel;

    @Column
    private String userPosition;

    @Column
    private String userInterestStack;

    @Column
    private String userLink;

    @Column
    private String userIntroduce;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, GITHUB

    // 소셜 고유 번호
    @Column(unique = true)
    private String socialId;

    @Column
    private String refreshToken;

    public User(String userEmail, String password,
                String userNickname, String userProfileImage,
                int userLevel, String userPosition,
                String userInterestStack, String userLink,
                String userIntroduce, SocialType socialType,
                String socialId, String refreshToken) {
        this.userEmail = userEmail;
        this.password = password;
        this.userNickname = userNickname;
        this.userProfileImage = userProfileImage;
        this.userLevel = userLevel;
        this.userPosition = userPosition;
        this.userInterestStack = userInterestStack;
        this.userLink = userLink;
        this.userIntroduce = userIntroduce;
        this.socialType = socialType;
        this.socialId = socialId;
        this.refreshToken = refreshToken;
    }
}
