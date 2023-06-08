package tra.orbit_be.security.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import tra.orbit_be.exception.CustomException;
import tra.orbit_be.exception.ErrorCode;
import tra.orbit_be.security.jwt.HeaderTokenExtractor;
import tra.orbit_be.security.jwt.JwtPreProcessingToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JwtAuthFilter : 서버에 접근시 JWT 확인 후 인증을 실시합니다.
 * Token을 내려주는 Filter가 아닌 client에서 받아지는 Token을 서버 사이드에서 검증하는 클래스
 * SecurityContextHolder 보관소에 해당
 * Token 값의 인증 상태를 보관하고 필요할 때마다 인증 확인 후 권한 상태 확인 하는 기능
 */
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {

    private final HeaderTokenExtractor extractor;

    public JwtAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher,
                         HeaderTokenExtractor extractor) {
        super(requiresAuthenticationRequestMatcher);
        this.extractor = extractor;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try {
            // JWT 값을 담아주는 변수 TokenPayload
            String tokenPayload = request.getHeader("Authorization");
            if (tokenPayload == null) {
                throw new CustomException(ErrorCode.AUTH_TOKEN_NOT_FOUND);
            }

            /**
             * - Token 값이 존재하는 경우 -
             * (Bearer ) 부분만 제거 후 token 값 반환
             */
            JwtPreProcessingToken jwtToken = new JwtPreProcessingToken(
                    extractor.extract(tokenPayload, request)
            );

            return super
                    .getAuthenticationManager()
                    .authenticate(jwtToken);
        } catch (CustomException e) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(e.getErrorCode().getHttpStatus().value());
            response.getWriter().println("{");
            response.getWriter().println("\"status\" : \"" + e.getErrorCode().getHttpStatus().value()+"\",");
            response.getWriter().println("\"errors\" : \"" + e.getErrorCode().getHttpStatus().name()+"\",");
            response.getWriter().println("\"code\" : \"" + e.getErrorCode().name()+"\",");
            response.getWriter().println("\"message\" : \"" + e.getErrorCode().getErrorMessage()+"\"");
            response.getWriter().println("}");
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        /**
         * SecurityContext 사용자 Token 저장소를 생성합니다.
         * SecurityContext에 사용자의 인증된 Token값을 저장합니다.
         */
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);

        // FilterChain chain 해당 필터가 실행 후 다른 필터도 실행할 수 있도록 연결시켜주는 메서드
        chain.doFilter(request,response);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        /**
         * 로그인을 한 상태에서 Token값을 주고받는 상황에 잘못된 Token 값이라면
         * 인증이 성공하지 못한 단계이기 때문에 잘못된 Token값을 제거합니다.
         * 모든 인증받은 Context 값이 삭제 됩니다.
         */
        SecurityContextHolder.clearContext();

        super.unsuccessfulAuthentication(request, response, failed);
    }
}
