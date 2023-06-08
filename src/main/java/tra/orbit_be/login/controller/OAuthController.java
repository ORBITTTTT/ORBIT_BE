package tra.orbit_be.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tra.orbit_be.exception.CustomException;
import tra.orbit_be.exception.ErrorCode;
import tra.orbit_be.login.service.KakaoUserService;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final KakaoUserService kakaoUserService;

    // 카카오 로그인
    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity kakaoLogin(@RequestParam String code,
                                     HttpServletResponse response) throws JsonProcessingException {
        try { // 회원가입 진행 성공시
            kakaoUserService.kakaoLogin(code, response);
            return new ResponseEntity("카카오 로그인 성공", HttpStatus.OK);
        } catch (Exception e) { // 에러나면 false
            throw new CustomException(ErrorCode.INVALID_KAKAO_LOGIN_ATTEMPT);
        }
    }
}
