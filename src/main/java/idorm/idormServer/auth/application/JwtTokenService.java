package idorm.idormServer.auth.application;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.in.JwtTokenUseCase;
import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtTokenService implements JwtTokenUseCase {

	private final Key signingKey;
	private final long accessTokenValidityMilliseconds;
	private final long refreshTokenValidityMilliseconds;

	@Autowired
	public JwtTokenService(@Value("${spring.security.jwt.token.secret-key}") String secretKey,
		@Value("${spring.security.jwt.token.expire-length.access}") long accessTokenValidityMilliseconds,
		@Value("${spring.security.jwt.token.expire-length.refresh}") long refreshTokenValidityMilliseconds) {

		byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
		this.signingKey = Keys.hmacShaKeyFor(keyBytes);
		this.accessTokenValidityMilliseconds = accessTokenValidityMilliseconds;
		this.refreshTokenValidityMilliseconds = refreshTokenValidityMilliseconds;
	}

	@Override
	public String createAccessToken(AuthResponse authInfo) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + accessTokenValidityMilliseconds);

		return Jwts.builder()
			.claim("id", authInfo.getId())
			.claim("role", authInfo.getRole())
			.claim("nickname", authInfo.getNickname())
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(SignatureAlgorithm.HS256, signingKey)
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
	public AuthResponse getParsedClaims(String token) {
		Claims claims;
		try {
			claims = Jwts.parser()
				.setSigningKey(signingKey)
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			Long id = (Long)e.getClaims().get("id");
			String role = (String)e.getClaims().get("role");
			String nickname = (String)e.getClaims().get("nickname");
			return new AuthResponse(id, role, nickname);
		}

		Long id = (Long)claims.get("id");
		String role = (String)claims.get("role");
		String nickname = (String)claims.get("nickname");
		return new AuthResponse(id, role, nickname);
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