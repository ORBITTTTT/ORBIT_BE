package tra.orbit_be.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

@Slf4j
@Component
public class HeaderTokenExtractor {

    /**
     * HEADER_PREFIX
     * header Authorization token 값의 표준이 되는 변수
     */
    public final String HEADER_PREFIX = "Bearer ";

    public String extract(String header, HttpServletRequest request) {
        /**
         * - Token 값이 올바르지 않은 경우 -
         * header 값이 비어있거나 또는 HEADER_PREFIX 값보다 짧은 경우
         * Exception(예외)을 던져주어야 합니다.
         */
        if (header == null || header.equals("") || header.length() < HEADER_PREFIX.length()) {
            log.info("error request : {}", request.getRequestURI());
            throw new NoSuchElementException("올바른 JWT 정보가 아닙니다.");
        }

        /**
         * - Token 값이 존재하는 경우 -
         * (Bearer ) 부분만 제거 후 token 값 반환
         */
        return header.substring(
                HEADER_PREFIX.length(),
                header.length()
        );
    }
}
