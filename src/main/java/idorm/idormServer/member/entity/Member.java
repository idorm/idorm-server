package idorm.idormServer.member.entity;

import static idorm.idormServer.email.entity.Email.*;
import static idorm.idormServer.matchingMate.entity.MatePreferenceType.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import idorm.idormServer.auth.application.port.out.EncryptPort;
import idorm.idormServer.auth.entity.RoleType;
import idorm.idormServer.calendar.entity.Team;
import idorm.idormServer.common.util.Validator;
import idorm.idormServer.community.comment.entity.Comment;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.entity.MatchingInfo;
import idorm.idormServer.matchingMate.entity.MatchingMate;
import idorm.idormServer.matchingMate.entity.MatchingMates;
import idorm.idormServer.photo.adapter.out.api.exception.NotFoundFileException;
import idorm.idormServer.report.entity.Report;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(value = EnumType.STRING)
	@Column(columnDefinition = "ENUM('ACTIVE', 'DELETED')")
	private MemberStatus memberStatus;

	@Column(nullable = false, length = MAX_EMAIL_LENGTH)
	private String email;

	@Embedded
	private Nickname nickname;

	@Embedded
	private Password password;

	private String profilePhotoUrl;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "ENUM('MEMBER', 'ADMIN')")
	private RoleType roleType;

	@OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
	@JoinColumn(name = "matching_info_id")
	private MatchingInfo matchingInfo;

	@Embedded
	private MatchingMates matchingMates;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	private List<Post> posts = new ArrayList<>();

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "reportedMember", fetch = FetchType.LAZY)
	private List<Report> reports = new ArrayList<>();

	@Builder
	public Member(String email, String password, String nickname, EncryptPort encryptPort) {
		validateConstructor(email);
		memberStatus = MemberStatus.ACTIVE;
		this.email = email;
		this.nickname = Nickname.from(nickname);
		this.password = Password.from(encryptPort, password);
		profilePhotoUrl = null;
		roleType = RoleType.MEMBER;
		matchingMates = MatchingMates.empty();
	}

	private void validateConstructor(final String email) {
		Validator.validateNotBlank(email);
	}

	public void updateNickname(final String newNickname) {
		this.nickname.update(newNickname);
	}

	public void updatePassword(final EncryptPort encryptPort, final String newPassword) {
		this.password.update(encryptPort, newPassword);
	}

	public boolean existsOfMemberPhoto() {
		return profilePhotoUrl != null;
	}

	public void updateMemberPhoto(final String photoUrl) {
		Validator.validateNotBlank(photoUrl);
		this.profilePhotoUrl = photoUrl;
	}

	public void deleteMemberPhoto() {
		if (this.profilePhotoUrl == null) {
			throw new NotFoundFileException();
		}
		this.profilePhotoUrl = null;
	}

	public void withdraw() {
		memberStatus = MemberStatus.DELETED;
		email = null;
		nickname = null;
		password = null;
		profilePhotoUrl = null;
		roleType = null;
		matchingMates = null;
		team = null;
		posts = null;
		comments = null;
	}

	public Set<MatchingMate> getFavoriteMates() {
		return Collections.unmodifiableSet(matchingMates.getFavoriteMates());
	}

	public Set<MatchingMate> getNonFavoriteMates() {
		return Collections.unmodifiableSet(matchingMates.getNonFavoriteMates());
	}

	public void addFavoriteMate(final MatchingMate matchingMate) {
		matchingMates.addFavoriteMate(matchingMate);
	}

	public void addNonFavoriteMate(final MatchingMate matchingMate) {
		matchingMates.addNonFavoriteMate(matchingMate);
	}

	public void deleteFavoriteMate(final MatchingMate matchingMate) {
		matchingMates.deleteFavoriteMate(matchingMate);
	}

	public void deleteNonFavoriteMate(final MatchingMate matchingMate) {
		matchingMates.deleteNonFavoriteMate(matchingMate);
	}

	public boolean isNonFavoriteMate(final Member targetMember) {
		return matchingMates.isNonFavoriteMate(MatchingMate.of(this, targetMember, NONFAVORITE));
	}
}