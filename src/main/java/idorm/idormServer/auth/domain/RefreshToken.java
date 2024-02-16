package idorm.idormServer.auth.domain;

import idorm.idormServer.auth.adapter.out.api.exception.UnAuthorizedRefreshTokenException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshToken {

	private Long id;
	private Long memberId;
	private String token;

	public RefreshToken(final Long memberId, final String token) {
		this.memberId = memberId;
		this.token = token;
	}

	public static RefreshToken forMapper(final Long id, final Long memberId, final String token) {
		return new RefreshToken(id, memberId, token);
	}

	public void validateSameToken(final String token) {
		if (!this.token.equals(token)) {
			throw new UnAuthorizedRefreshTokenException();
		}
	}
}
