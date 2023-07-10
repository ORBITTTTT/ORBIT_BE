package tra.orbit_be.login.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tra.orbit_be.login.dto.OAuthUserInfoDto;
import tra.orbit_be.login.enums.SocialType;
import tra.orbit_be.login.domain.User;
import tra.orbit_be.login.repository.OAuthRepository;
import tra.orbit_be.security.UserDetailsImpl;
import tra.orbit_be.security.jwt.JwtTokenUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubUserService {

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    String githubClientId;
    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    String githubClientSecret;
    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
    String githubRedirect;

    private final PasswordEncoder passwordEncoder;
    private final OAuthRepository oAuthRepository;

    public void githubLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        log.info("깃허브 로그인 1번 접근");
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 깃허브 API 호출
        log.info("깃허브 로그인 2번 접근");
        OAuthUserInfoDto githubUserInfo = getGithubUserInfo(accessToken);

        // 3. 필요시에 회원가입
        log.info("깃허브 로그인 3번 접근");
        User githubUser = resigterGithubUserIfNeeded(githubUserInfo);

        // 4. 강제 로그인 처리 & jwt 토큰 발급
        log.info("깃허브 로그인 4번 접근");
        jwtTokenCreate(githubUser, response);
    }

    // 1. "인가 코드"로 "액세스 토큰" 요청
    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", githubClientId); // 본인의 REST API키
        body.add("client_secret", githubClientSecret);
        body.add("redirect_uri", githubRedirect); //성공 후 리다이렉트 되는 곳
        body.add("code", code);

        // POST 요청 보내기
        HttpEntity<MultiValueMap<String, String>> githubTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                githubTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        // response에서 accessToken 가져오기
        String responseBody = response.getBody();
        // responseBody에서 access_token 부분 꺼내기
        assert responseBody != null;
        String responseToken = responseBody.substring(13);
        String[] access_token = responseToken.split("&");
        return access_token[0];
    }

    // 2. 토큰으로 깃허브 API 호출
    private OAuthUserInfoDto getGithubUserInfo(String accessToken) throws JsonProcessingException {
        // 헤더에 액세스 토큰 담기, Content-type 지정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> githubUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                githubUserInfoRequest,
                String.class
        );

        // response에서 유저정보 가져오기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        // 깃허브 유저 고유 아이디값
        String socialId = jsonNode.get("id").asText();

        // nickname
        Random random = new Random();
        String rdname = "";
        for (int i = 0; i < 8; i++) {
            rdname += String.valueOf(random.nextInt(10));
        }
        String nickname = "G_" + rdname;

        // 이메일 값 필수
        String email = jsonNode.get("email").asText();

        // 이메일 값이 null 일 때
        if (Objects.equals(email, "null")) {
            email = nickname + "@gmail.com";
        }

        // 기본 이미지
        // TODO 깃허브 - ORBIT 기본 이미지 바꾸기
        String profileImage = null;

        return new OAuthUserInfoDto(socialId, nickname, email, profileImage);
    }

    // 3. 필요시에 회원가입
    private User resigterGithubUserIfNeeded(OAuthUserInfoDto githubUserInfo) {
        // 가입된 사용자인지 소셜id로 찾기
        String githubSocialId = githubUserInfo.getSocialId();
        User githubUser = oAuthRepository.findBySocialId(githubSocialId)
                .orElse(null);

        // TODO 입력된 값이 DB에 저장되도록 바꿔야함
        // null값 오면 회원가입 가능 기존 사용자가 없다는 뜻
        if (githubUser == null) {
            String nickname = githubUserInfo.getNickname();
            String email = githubUserInfo.getEmail();
            String password = UUID.randomUUID().toString(); // password : random UUID
            String encodedPassword = passwordEncoder.encode(password); // 비밀번호 암호화
            String profileImage = githubUserInfo.getProfileImage();

            githubUser = new User(email, encodedPassword,
                    nickname, profileImage, 0, null,
                    null, null, nickname + "입니다.",
                    SocialType.GITHUB, githubSocialId, null);
            oAuthRepository.save(githubUser);
        }

        return githubUser;
    }

    // 4. 강제 로그인 처리 & jwt 토큰 발급
    private void jwtTokenCreate(User gitgubUser, HttpServletResponse response) {
        UserDetails userDetails = new UserDetailsImpl(gitgubUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 강제로그인 시도까지 함, 여기까진 평범한 로그인과 같음

        // 여기부터 토큰 프론트에 넘기는것
        UserDetailsImpl userDetails1 = (UserDetailsImpl) authentication.getPrincipal();
        String token = JwtTokenUtils.generateJwtToken(userDetails1);
        response.addHeader("Authorization", "BEARER " + token);

    }
}
