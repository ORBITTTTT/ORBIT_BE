package tra.orbit_be.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    /*
    401 UNAUTHORIZED : 인증되지 않은 사용자
    */
    AUTH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다"),
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "만료되었거나 유효하지 않은 토큰입니다"),
    INVALID_LOGIN_ATTEMPT(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다"),
    INVALID_KAKAO_LOGIN_ATTEMPT(HttpStatus.UNAUTHORIZED, "카카오 로그인에 실패하였습니다"),
    INVALID_GITHUB_LOGIN_ATTEMPT(HttpStatus.UNAUTHORIZED, "깃허브 로그인에 실패하였습니다"),
    /*
    403 FORBIDDEN : 권한이 없는 사용자
    */
    INVALID_AUTHORITY(HttpStatus.FORBIDDEN,"권한이 없는 사용자 입니다"),
    /*
    404 not found
     */
    API_NOT_FOUND(HttpStatus.NOT_FOUND, "잘못된 주소입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다");


    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
