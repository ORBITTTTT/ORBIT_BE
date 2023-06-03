package tra.orbit_be.service.login;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@Slf4j
@Service
public class UserService {

    public String getKakaoAccessToken(String code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=14b2bcb493f4aaa5cd07550c063e8d6d"); // TODO REST_API_KEY 입력
            sb.append("&client_secret=4Xl8MMCP0I9ysqyRH7dYVX78d4NmPtrO"); // TODO Client Secret 입력
            sb.append("&redirect_uri=http://localhost:8080/oauth/kakao/callback"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            // 결과 코드가 200 이라면 성공
            int responseCode = conn.getResponseCode();
            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            log.info("responseCode={}", responseCode);
            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body = {}", result);

            // Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            log.info("access_token = {}", access_Token);
            log.info("refresh_token = {}", refresh_Token);

            br.close();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    public void createKakaoUser(String token) {

        String reqURL = "https://kapi.kakao.com/v2/user/me";

        // access_token 을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); // 전송할 header 작성, access_token 전송

            // 결과 코드가 200 이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode = {}", responseCode);

            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body = {}", result);

            // Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            Long id = element.getAsJsonObject().get("id").getAsLong();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account")
                    .getAsJsonObject().get("has_email").getAsBoolean();
            String email = "";
            if (hasEmail) {
                email = element.getAsJsonObject().get("kakao_account")
                        .getAsJsonObject().get("email").getAsString();
            }

            log.info("id = {}", id);
            log.info("email = {}", email);

            br.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
