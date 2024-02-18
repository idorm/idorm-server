package idorm.idormServer.member.domain;

import java.time.LocalDateTime;
import java.util.List;

import idorm.idormServer.auth.domain.RoleType;
import idorm.idormServer.common.util.Validator;
import idorm.idormServer.matchingMate.domain.MatchingMates;
import idorm.idormServer.photo.adapter.out.api.exception.NotFoundFileException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {

	private Long id;
	private MemberStatus memberStatus;
	private String email;
	private Nickname nickname;
	private Password password;
	private MemberPhoto memberPhoto;
	private RoleType roleType;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private MatchingMates matchingMates;

	@Builder
	public Member(String email, Password password, Nickname nickname) {
		validateConstructor(email, password, nickname);
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		memberStatus = MemberStatus.ACTIVE;
		roleType = RoleType.MEMBER;
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
		matchingMates = MatchingMates.empty();
	}

	public static Member admin(String email, Password password, Nickname nickname) {
		Member member = new Member(email, password, nickname);
		member.roleType = RoleType.ADMIN;
		return member;
	}

	private static void validateConstructor(final String email, final Password password, final Nickname nickname) {
		Validator.validateNotBlank(email);
		Validator.validateNotNull(List.of(password, nickname));
	}

	public static Member forMapper(final Long id,
		final MemberStatus memberStatus,
		final String email,
		final Nickname nickname,
		final Password password,
		final MemberPhoto memberPhoto,
		final RoleType roleType,
		final LocalDateTime createdAt,
		final LocalDateTime updatedAt,
		final MatchingMates matchingMates) {
		return new Member(id, memberStatus, email, nickname, password, memberPhoto, roleType, createdAt, updatedAt,
			matchingMates);
	}

	public void assignId(Long generatedId) {
		this.id = generatedId;
	}

	public void updateNickname(final Nickname nickname) {
		this.nickname.validateValidUpdate(LocalDateTime.now());
		this.nickname = nickname;
	}

	public void updatePassword(final Password password) {
		this.password = password;
	}

	public boolean existsOfMemberPhoto() {
		return memberPhoto != null;
	}

	public void updateMemberPhoto(final String photoUrl) {
		this.memberPhoto = new MemberPhoto(photoUrl);
	}

	public void deleteMemberPhoto() {
		if (this.memberPhoto == null) {
			throw new NotFoundFileException();
		}
		this.memberPhoto = null;
	}

	public void withdraw() {
		memberStatus = MemberStatus.DELETED;
		email = null;
		nickname = null;
		password = null;
		memberPhoto = null;
		roleType = null;
		updatedAt = LocalDateTime.now();
		matchingMates = null;
	}
}