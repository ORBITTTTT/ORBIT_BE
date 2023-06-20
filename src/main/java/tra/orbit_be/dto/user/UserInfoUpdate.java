package tra.orbit_be.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tra.orbit_be.model.user.InterestStack;
import tra.orbit_be.model.user.Position;
import tra.orbit_be.model.user.ProfileLink;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoUpdate {

    // 프로필 이미지
    private String userProfileImage;
    // 닉네임
    private String userNickname;
    // 직군
    private List<Position> userPositions;
    // 관심 기술
    private List<InterestStack> userInterestStacks;
    // 자기소개
    private String userIntroduce;
    // 유저 깃허브, 블로그 주소
    private List<ProfileLink> userLinks;
}
