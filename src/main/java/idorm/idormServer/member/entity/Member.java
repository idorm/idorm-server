package idorm.idormServer.member.entity;

import static idorm.idormServer.email.entity.Email.MAX_EMAIL_LENGTH;

import idorm.idormServer.auth.application.port.out.EncryptPort;
import idorm.idormServer.auth.entity.RoleType;
import idorm.idormServer.calendar.adapter.out.exception.AlreadyRegisteredTeamException;
import idorm.idormServer.calendar.entity.Team;
import idorm.idormServer.common.util.Validator;
import idorm.idormServer.community.comment.entity.Comment;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.adapter.out.exception.IllegalStatementMatchingInfoNonPublicException;
import idorm.idormServer.matchingInfo.adapter.out.exception.NotFoundMatchingInfoException;
import idorm.idormServer.matchingInfo.entity.MatchingInfo;
import idorm.idormServer.matchingMate.adapter.out.exception.DuplicatedFavoriteMateException;
import idorm.idormServer.matchingMate.adapter.out.exception.DuplicatedNonFavoriteMateException;
import idorm.idormServer.matchingMate.adapter.out.exception.NotFoundDisLikedMemberException;
import idorm.idormServer.matchingMate.adapter.out.exception.NotFoundLikedMemberException;
import idorm.idormServer.matchingMate.entity.MatchingMate;
import idorm.idormServer.matchingMate.entity.MatePreferenceType;
import idorm.idormServer.photo.adapter.out.api.exception.NotFoundFileException;
import idorm.idormServer.report.entity.Report;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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

  @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      orphanRemoval = true)
  private List<MatchingMate> matchingMates = new ArrayList<>();

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
  }

  private void validateConstructor(final String email) {
    Validator.validateNotBlank(email);
  }

  public void updateNickname(final String newNickname) {
    this.nickname.update(newNickname);
  }

  public void updatePassword(EncryptPort encryptPort, String newPassword) {
    this.password.update(encryptPort, newPassword);
  }

  public boolean existsOfMemberPhoto() {
    return profilePhotoUrl != null;
  }

  public void updateMemberPhoto(final String photoUrl) {
    Validator.validateNotBlank(photoUrl);
    this.profilePhotoUrl = photoUrl;
  }

  public void updateMatchingInfo(final MatchingInfo matchingInfo) {
    this.matchingInfo = matchingInfo;
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

  public List<MatchingMate> getFavoriteMates() {
    List<MatchingMate> results = this.matchingMates.stream()
        .filter(MatchingMate::isPublic)
        .filter(MatchingMate::isFavorite)
        .collect(Collectors.toUnmodifiableList());
    return results;
  }

  public List<MatchingMate> getNonFavoriteMates() {
    List<MatchingMate> results = this.matchingMates.stream()
        .filter(MatchingMate::isPublic)
        .filter(MatchingMate::isNonFavorite)
        .collect(Collectors.toUnmodifiableList());
    return results;
  }

  public boolean isNotNonFavoriteMate(final MatchingInfo matchingInfo) {
    boolean result = matchingMates.stream()
        .anyMatch(mate -> mate.isNonFavorite(matchingInfo));
    return !result;
  }

  public void addReport(final Report report) {
    this.reports.add(report);
  }

  public void deleteMatchingInfo() {
    if (matchingInfo == null) {
      throw new NotFoundMatchingInfoException();
    }
    matchingInfo = null;
  }

  public void addMate(final MatchingMate newMate) {
    Optional<MatchingMate> existMate = getDuplicateMate(newMate);
    if (existMate.isEmpty()) {
      this.matchingMates.add(newMate);
      return;
    }

    validateDuplicateTypeMate(existMate.get(), newMate);

    this.matchingMates.remove(existMate.get());
    this.matchingMates.add(newMate);
  }

  public void deleteMate(final MatchingInfo matchingInfo, final MatePreferenceType preferenceType) {
    Optional<MatchingMate> deleteMate = this.matchingMates.stream()
        .filter(matchingMate -> matchingMate.getMatchingInfoForTarget().equals(matchingInfo) &&
            matchingMate.getPreferenceType().equals(preferenceType) &&
            matchingMate.getOwner().equals(this)
        )
        .findAny();
    if (deleteMate.isEmpty()) {
      handleNotFoundMate(preferenceType);
    }
    this.matchingMates.remove(deleteMate.get());
  }

  private Optional<MatchingMate> getDuplicateMate(MatchingMate matchingMate) {
    Optional<MatchingMate> duplicateMate = this.matchingMates.stream()
        .filter(mate -> mate.equals(matchingMate))
        .findAny();
    return duplicateMate;
  }

  public void removeTeam() {
    this.team = null;
  }

  public void addTeam(Team team) {
    this.team = team;
  }

  public void validateExistTeam() {
    if (this.getTeam() != null) {
      throw new AlreadyRegisteredTeamException();
    }
  }

  private void validateDuplicateTypeMate(MatchingMate existMate, MatchingMate newMate) {
    if (existMate.getPreferenceType().equals(newMate.getPreferenceType())) {
      handleDuplicateMate(newMate.getPreferenceType());
    }
  }

  public void validateMatchingStatement() {
    if (Objects.isNull(matchingInfo) || !matchingInfo.getIsPublic()) {
      throw new IllegalStatementMatchingInfoNonPublicException();
    }
  }

  private void handleNotFoundMate(MatePreferenceType preferenceType) {
    if (preferenceType.isFavorite()) {
      throw new NotFoundLikedMemberException();
    } else {
      throw new NotFoundDisLikedMemberException();
    }
  }

  private void handleDuplicateMate(MatePreferenceType preferenceType) {
    if (preferenceType.isFavorite()) {
      throw new DuplicatedFavoriteMateException();
    } else {
      throw new DuplicatedNonFavoriteMateException();
    }
  }
}
