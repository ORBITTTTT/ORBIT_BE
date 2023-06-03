package tra.orbit_be.controller.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tra.orbit_be.service.login.UserService;

@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/kakao/callback")
    public void kakaoCallback(@RequestParam String code) {
        log.info(code);
        String access_Token = userService.getKakaoAccessToken(code);
        userService.createKakaoUser(access_Token);
    }
}
