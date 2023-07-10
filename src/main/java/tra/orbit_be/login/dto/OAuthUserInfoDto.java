package tra.orbit_be.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tra.orbit_be.login.domain.User;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OAuthUserInfoDto {

    private String socialId;
    private String nickname;
    private String email;
    private String profileImage;

    public OAuthUserInfoDto(User user) {
        this.socialId = user.getSocialId();
        this.nickname = user.getUserNickname();
        this.email = user.getUserEmail();
        this.profileImage = user.getUserProfileImage();
    }
}
