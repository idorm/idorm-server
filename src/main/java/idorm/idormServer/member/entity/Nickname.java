package idorm.idormServer.member.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.member.adapter.out.MemberResponseCode;
import idorm.idormServer.member.adapter.out.exception.CannotUpdateNicknameException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Nickname {

	private static final String NICKNAME_REGEX = "^[A-Za-z0-9ㄱ-ㅎ가-힣]{2,8}+$";
	private static final int VALID_UPDATE_DAY = 30;

	@Column(name = "nickname", nullable = false)
	private String value;

	@CreatedDate
	@Column(name = "nickname_updated_at")
	@JsonIgnore
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

	private Nickname(final String value) {
		validate(value);
		this.value = value;
		this.updatedAt = LocalDateTime.now();
	}

	static Nickname from(final String value) {
		return new Nickname(value);
	}

	private void validate(final String value) {
		Validator.validateNotBlank(value);
		Validator.validateFormat(value, NICKNAME_REGEX, MemberResponseCode.INVALID_NICKNAME_CHARACTER);

	}

	void update(final String newNickname) {
		validateValidUpdate();
		this.value = newNickname;
	}

	private void validateValidUpdate() {
		final LocalDateTime now = LocalDateTime.now();
		if (now.isBefore(this.updatedAt.plusDays(VALID_UPDATE_DAY))) {
			throw new CannotUpdateNicknameException();
		}
	}
}