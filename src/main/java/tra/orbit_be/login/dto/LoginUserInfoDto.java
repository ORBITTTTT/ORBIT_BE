package tra.orbit_be.login.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginUserInfoDto {
    // 로그인 시 body로 내려가는 사용자 정보

    private Long userTableId;
    private String userEmail;
    private String userNickname;
    private boolean login;
    private String accessToken;
    private String userProfileImage;

    // 일반 로그인할 떄 프론트에 내려주는 값

    public LoginUserInfoDto(Long userTableId,
                            String userEmail,
                            String userNickname,
                            boolean login,
                            String accessToken,
                            String userProfileImage) {
        this.userTableId = userTableId;
        this.userEmail = userEmail;
        this.userNickname = userNickname;
        this.login = login;
        this.accessToken = accessToken;
        this.userProfileImage = userProfileImage;
    }
}
