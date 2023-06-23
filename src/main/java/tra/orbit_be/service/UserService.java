package tra.orbit_be.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tra.orbit_be.dto.user.UserInfoUpdate;
import tra.orbit_be.exception.CustomException;
import tra.orbit_be.exception.ErrorCode;
import tra.orbit_be.login.model.User;
import tra.orbit_be.login.repository.OAuthRepository;
import tra.orbit_be.model.user.InterestStack;
import tra.orbit_be.model.user.Position;
import tra.orbit_be.model.user.ProfileLink;
import tra.orbit_be.repository.PositionRepository;
import tra.orbit_be.repository.InterestStackRepository;
import tra.orbit_be.repository.ProfileLinkRepository;
import tra.orbit_be.security.UserDetailsImpl;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    // User 테이블 사용하는 Repository
    private final OAuthRepository userRepository;
    private final PositionRepository positionRepository;
    private final InterestStackRepository interestStackRepository;
    private final ProfileLinkRepository profileLinkRepository;

    @Transactional
    public void userInfoUpdate(UserDetailsImpl userDetails, UserInfoUpdate userInfo) {
        /**
         * 1. user 소셜ID로 사용자 찾기
         * 2. 있으면 사용자 정보 업데이트, 없으면 에러문구 내려주기
         * 3. userRepository에 save
         */
        // 1. 사용자 찾기
        String socialId = userDetails.getUser().getSocialId();
        Optional<User> user = userRepository.findBySocialId(socialId);

        // 사용자가 없을 경우
        if (!user.isPresent()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 사진이 null일 경우
        String defautImage = "기본 이미지";
        String userProfileImage =
                (userInfo.getUserProfileImage() == null) ? defautImage : userInfo.getUserProfileImage();
        // 닉네임
        String userNickname =
                (userInfo.getUserNickname() == null)
                        ? userDetails.getUser().getUserNickname() : userInfo.getUserNickname();

        // 자기소개
        String userIntroduce =
                (userInfo.getUserIntroduce() == null)
                        ? userNickname + " 입니다." : userInfo.getUserIntroduce();

        // 직군(Positoin) 저장
        positionSave(userDetails, userInfo);
        // 기술 스택(InterestStack) 저장
        stackSave(userDetails, userInfo);
        // 프로필 링크(ProfileLink) 저장
        profileLinkSave(userDetails, userInfo);

        // 2. 받아온 사용자 정보 userInfo -> User객체로 변경하기
        user.get().updateProfile(userInfo);

        // 3. 업데이트 정보 저장
        userRepository.save(user.get());
    }

    // 직군(Positoin) 저장
    private void positionSave(UserDetailsImpl userDetails, UserInfoUpdate userInfo) {
        for (int i = 0; i < userInfo.getUserPositions().size(); i++) {
            Position position = Position.builder()
                    .posName(userInfo.getUserPositions().get(i).getPosName())
                    .user(userDetails.getUser())
                    .build();
            // Position 저장
            positionRepository.save(position);
        }
    }

    // 기술 스택(InterestStack) 저장
    private void stackSave(UserDetailsImpl userDetails, UserInfoUpdate userInfo) {
        for (int i = 0; i < userInfo.getUserInterestStacks().size(); i++) {
            InterestStack stack = InterestStack.builder()
                    .stackName(userInfo.getUserInterestStacks().get(i).getStackName())
                    .user(userDetails.getUser())
                    .build();
            // Position 저장
            interestStackRepository.save(stack);
        }
    }

    // 프로필 링크(ProfileLink) 저장
    private void profileLinkSave(UserDetailsImpl userDetails, UserInfoUpdate userInfo) {
        for (int i = 0; i < userInfo.getUserLinks().size(); i++) {
            ProfileLink profileLink = ProfileLink.builder()
                    .profileLinkTitle(userInfo.getUserLinks().get(i).getProfileLinkTitle())
                    .profileLink(userInfo.getUserLinks().get(i).getProfileLink())
                    .user(userDetails.getUser())
                    .build();
            // Position 저장
            profileLinkRepository.save(profileLink);
        }
    }
}
