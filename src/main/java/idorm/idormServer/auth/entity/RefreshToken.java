package idorm.idormServer.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import idorm.idormServer.auth.adapter.out.api.exception.UnAuthorizedRefreshTokenException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "refresh_token_id")
	private Long id;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "refresh_token", nullable = false)
	private String token;

	public RefreshToken(final Long memberId, final String token) {
		this.memberId = memberId;
		this.token = token;
	}

	public void validateSameToken(final String token) {
		if (!this.token.equals(token)) {
			throw new UnAuthorizedRefreshTokenException();
		}
	}
}
