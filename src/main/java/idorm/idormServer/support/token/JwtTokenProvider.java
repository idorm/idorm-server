package idorm.idormServer.support.token;

import idorm.idormServer.auth.dto.AuthInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements TokenManager {

    private final Key signingKey;
    private final long accessTokenValidityMilliseconds;
    private final long refreshTokenValidityMilliseconds;

    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") String secretKey,
                            @Value("${security.jwt.token.expire-length.access}") long accessTokenValidityMilliseconds,
                            @Value("${security.jwt.token.expire-length.refresh}") long refreshTokenValidityMilliseconds) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityMilliseconds = accessTokenValidityMilliseconds;
        this.refreshTokenValidityMilliseconds = refreshTokenValidityMilliseconds;
    }


    @Override
    public String createAccessToken(AuthInfo authInfo) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityMilliseconds);

        return Jwts.builder()
                .claim("id", authInfo.getId())
                .claim("role", authInfo.getRole())
                .claim("nickname", authInfo.getNickname())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, signingKey) // TODO: 알고리즘 대체?
                .compact();
    }

    @Override
    public String createRefreshToken() {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityMilliseconds);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }

    @Override
    public AuthInfo getParsedClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            Long id = (Long) e.getClaims().get("id");
            String role = (String) e.getClaims().get("role");
            String nickname = (String) e.getClaims().get("nickname");
            return new AuthInfo(id, role, nickname);
        }

        Long id = (Long) claims.get("id");
        String role = (String) claims.get("role");
        String nickname = (String) claims.get("nickname");
        return new AuthInfo(id, role, nickname);
    }

    @Override
    public boolean isValid(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
