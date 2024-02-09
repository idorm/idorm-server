package idorm.idormServer.support.token;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorizationExtractor {

    public static final String BEARER_TYPE = "Bearer";

    public static String extractAccessToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(HttpHeaders.AUTHORIZATION);
        return extract(headers);
    }

    public static String extractRefreshToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders("Refresh-Token");
        return extract(headers);
    }

    private static String extract(Enumeration<String> headers) {
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if (value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
                String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();

                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }
        return null;
    }
}
