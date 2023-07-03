package tra.orbit_be.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tra.orbit_be.dto.user.UserInfoUpdate;
import tra.orbit_be.security.UserDetailsImpl;
import tra.orbit_be.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 로그인 후 바로 정보 입력
    @ApiOperation(value = "회원 정보 업데이트", notes = "로그인 직후 또는 마이페이지 회원 정보 수정")
    @PostMapping("/users/profile")
    public ResponseEntity<String> userInfoUpdate(
            @ApiParam(value = "로그인된 회원만 접근 가능한 회원 권한 여부 판단")
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @ApiParam(value = "회원 정보 수정, 프로필 이미지/닉네임/직군/기술 스택/자기 소개/프로필 링크 필요", required = true)
            @RequestBody UserInfoUpdate userInfo) {

         return userService.userInfoUpdate(userDetails, userInfo);
    }

    // 닉네임 중복 검사
    @ApiOperation(value = "닉네임 중복 검사", notes = "단순히 닉네임 중복만 검사")
    @PostMapping("/users/profile/nickCheck")
    public ResponseEntity<String> checkNickname(
            @ApiParam(value = "닉네임", required = true)
            @RequestParam String nickName) {
        return userService.checkNickname(nickName);
    }
}
