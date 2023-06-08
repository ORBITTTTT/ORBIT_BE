package tra.orbit_be.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import tra.orbit_be.login.dto.LoginUserInfoDto;
import tra.orbit_be.login.model.User;
import tra.orbit_be.security.jwt.JwtTokenUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "BEARER";
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // Token 생성
        final String token = JwtTokenUtils.generateJwtToken(userDetails);
        log.info("{} 's token : {} {}", userDetails.getUser().getUserEmail(), TOKEN_TYPE, token);
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);
        log.info("LOGIN SUCCESS!");

        // TODO User정보 내려줄 때 작성하기
//        response.setContentType("application/json; charset=utf-8");
//        User user = userDetails.getUser();
//        LoginUserInfoDto loginUserInfoDto =
//                new LoginUserInfoDto(user.getUserTableId(),
//                        user.getUserEmail(),
//                        user.getUserNickname(),
//                        true,
//                        token,
//                        user.getUserProfileImage());
//        String result = mapper.writeValueAsString(loginUserInfoDto);
//        response.getWriter().write(result);
    }
}
