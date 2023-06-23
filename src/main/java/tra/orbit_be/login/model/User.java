package tra.orbit_be.login.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import tra.orbit_be.domain.Timestamped;
import tra.orbit_be.dto.user.UserInfoUpdate;
import tra.orbit_be.login.enums.SocialType;
import tra.orbit_be.model.user.InterestStack;
import tra.orbit_be.model.user.Position;
import tra.orbit_be.model.user.ProfileLink;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userTableId;

    // 유저 이메일
    @Column(nullable = false, unique = true)
    private String userEmail;

    // 유저 패스워드
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    // 유저 닉네임
    @Column(nullable = false, unique = true)
    private String userNickname;

    // 유저 이미지
    @Column
    private String userProfileImage;

    // 유저 단계
    @Column
    private int userLevel;

    // 직군
    @Column
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Position> userPositions;

    // 관심 기술
    @Column
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<InterestStack> userInterestStacks;

    // 유저 깃허브, 블로그 주소
    @Column
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ProfileLink> userLinks;

    // 자기소개
    @Column(length = 1000)
    private String userIntroduce;

    // 소셜 로그인
    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, GITHUB

    // 소셜 고유 번호
    @Column(unique = true)
    private String socialId;

    // 로그인 리프레시 토큰
    @Column
    private String refreshToken;

    public User(String userEmail, String password,
                String userNickname, String userProfileImage,
                int userLevel, List<Position> userPositions,
                List<InterestStack> userInterestStacks, List<ProfileLink> userLinks,
                String userIntroduce, SocialType socialType,
                String socialId, String refreshToken) {
        this.userEmail = userEmail;
        this.password = password;
        this.userNickname = userNickname;
        this.userProfileImage = userProfileImage;
        this.userLevel = userLevel;
        this.userPositions = userPositions;
        this.userInterestStacks = userInterestStacks;
        this.userLinks = userLinks;
        this.userIntroduce = userIntroduce;
        this.socialType = socialType;
        this.socialId = socialId;
        this.refreshToken = refreshToken;
    }

    // 로그인 직후 정보 수정
    public void updateProfile(UserInfoUpdate userInfo,
                              String userProfileImage,
                              String userNickname,
                              String userIntroduce) {
        this.userNickname = userNickname;
        this.userProfileImage = userProfileImage;
        this.userPositions = userInfo.getUserPositions();
        this.userInterestStacks = userInfo.getUserInterestStacks();
        this.userLinks = userInfo.getUserLinks();
        this.userIntroduce = userIntroduce;
    }
}
