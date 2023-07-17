package tra.orbit_be.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tra.orbit_be.exception.CustomException;
import tra.orbit_be.exception.ErrorCode;
import tra.orbit_be.login.service.GithubUserService;
import tra.orbit_be.login.service.KakaoUserService;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final KakaoUserService kakaoUserService;
    private final GithubUserService githubUserService;

    // 카카오 로그인
    @ApiOperation(value = "카카오 로그인", notes = "카카오 로그인")
    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity kakaoLogin(
            @ApiParam(value = "카카오에서 넘어온 code", required = true) @RequestParam String code,
            HttpServletResponse response) throws JsonProcessingException {
        // Header 추가 설정
        HttpHeaders resHeaders = getHttpHeaders();

        try { // 회원가입 진행 성공시
            kakaoUserService.kakaoLogin(code, response);
            return new ResponseEntity("카카오 로그인 성공", resHeaders, HttpStatus.OK);
        } catch (Exception e) { // 에러나면 false
            throw new CustomException(ErrorCode.INVALID_KAKAO_LOGIN_ATTEMPT);
        }
    }

    // 깃허브 로그인
    @ApiOperation(value = "깃허브 로그인", notes = "깃허브 로그인")
    @GetMapping("/oauth/github/callback")
    public ResponseEntity githubLogin(
            @ApiParam(value = "깃허브에서 넘어온 code", required = true) @RequestParam String code,
            HttpServletResponse response) throws JsonProcessingException {
        // Header 추가 설정
        HttpHeaders resHeaders = getHttpHeaders();

        try { // 회원가입 진행 성공시
            githubUserService.githubLogin(code, response);
            return new ResponseEntity("깃허브 로그인 성공", resHeaders, HttpStatus.OK);
        } catch (Exception e) { // 에러나면 false
            System.out.println(e.getMessage());
            throw new CustomException(ErrorCode.INVALID_GITHUB_LOGIN_ATTEMPT);
        }
    }

    // Header 추가 설정
    private HttpHeaders getHttpHeaders() {
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        return resHeaders;
    }
}
