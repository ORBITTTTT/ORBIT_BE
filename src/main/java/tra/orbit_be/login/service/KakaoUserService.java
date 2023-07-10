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
import java.util.Random;
import java.util.UUID;

/**
 * 카카오 로그인
 * 카카오 로그인 잘 되는지 확인하기
 * https://kauth.kakao.com/oauth/authorize?client_id={REST_API_KEY}&redirect_uri={REDIRECT_URI}&response_type=code
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoUserService {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    String kakaoClientId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    String kakaoClientSecret;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    String kakaoRedirect;

    private final PasswordEncoder passwordEncoder;
    private final OAuthRepository oAuthRepository;

    public void kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        log.info("카카오 로그인 1번 접근");
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        log.info("카카오 로그인 2번 접근");
        OAuthUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. 필요시에 회원가입
        log.info("카카오 로그인 3번 접근");
        User kakaoUser = resigterKakaoUserIfNeeded(kakaoUserInfo);

        // 4. 강제 로그인 처리 & jwt 토큰 발급
        log.info("카카오 로그인 4번 접근");
        jwtTokenCreate(kakaoUser, response);
    }

    // 1. "인가 코드"로 "액세스 토큰" 요청
    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId); // 본인의 REST API키
        body.add("client_secret", kakaoClientSecret);
        body.add("redirect_uri", kakaoRedirect); // 성공 후 리다이렉트 되는 곳
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseToken = objectMapper.readTree(responseBody);
        return responseToken.get("access_token").asText();
    }

    // 2. 토큰으로 카카오 API 호출
    private OAuthUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // response에서 유저 정보 가져오기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        // 카카오 유저 고유 아이디값
        String socialId = jsonNode.get("id").asText();

        // nickname
        Random random = new Random();
        String rdname = "";
        for (int i = 0; i < 8; i++) {
            rdname += String.valueOf(random.nextInt(10));
        }
        String nickname = "K_" + rdname;

        // 이메일 값 필수
        String email = jsonNode.get("kakao_account").get("email").asText();

        // 기본 이미지
        // TODO 카카오 - ORBIT 기본 이미지 바꾸기
        String profileImage = null;

        return new OAuthUserInfoDto(socialId, nickname, email, profileImage);
    }

    // 3. 필요시에 회원가입
    private User resigterKakaoUserIfNeeded(OAuthUserInfoDto kakaoUserInfo) {
        // socialId로 이미 가입한 회원인지 아닌지 구분하기
        String kakaoSocialId = kakaoUserInfo.getSocialId();
        User kakaoUser = oAuthRepository.findBySocialId(kakaoSocialId)
                .orElse(null);

        // TODO 입력된 값이 DB에 저장되도록 바꿔야함
        // null값 오면 회원가입 가능 기존 사용자가 없다는 뜻
        if (kakaoUser == null) {
            String nickname = kakaoUserInfo.getNickname();
            String email = kakaoUserInfo.getEmail();
            String password = UUID.randomUUID().toString(); // password : random UUID
            String encodedPassword = passwordEncoder.encode(password); // 비밀번호 암호화
            String profileImage = kakaoUserInfo.getProfileImage();

            kakaoUser = new User(email, encodedPassword,
                    nickname, profileImage, 0, null,
                    null, null, nickname + "입니다.",
                    SocialType.KAKAO, kakaoSocialId, null);
            oAuthRepository.save(kakaoUser);
        }

        return kakaoUser;
    }

    // 4. 강제 로그인 처리 & jwt 토큰 발급
    private void jwtTokenCreate(User kakaoUser, HttpServletResponse response) {
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 강제 로그인 시도까지 함, 여기까진 평범한 로그인과 같음

        // 여기부터 토큰 프론트에 넘기는 것
        UserDetailsImpl userDetails1 = (UserDetailsImpl) authentication.getPrincipal();
        String token = JwtTokenUtils.generateJwtToken(userDetails1);
        response.addHeader("Authorization", "BEARER " + token);
        System.out.println("token = " + token);
    }
}
