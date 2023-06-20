package tra.orbit_be.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PostMapping("/users/profile")
    public ResponseEntity<String> userInfoUpdate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserInfoUpdate userInfo) {

        userService.userInfoUpdate(userDetails, userInfo);

        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
