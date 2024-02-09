package idorm.idormServer.support.token;

import idorm.idormServer.auth.dto.AuthInfo;

public interface TokenManager {

    String createAccessToken(AuthInfo authInfo);

    String createRefreshToken();

    AuthInfo getParsedClaims(String token);

    boolean isValid(String token);
}
