package idorm.idormServer.member.domain;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.matchingMate.domain.MatchingMates;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
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
        roleType = RoleType.USER;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        matchingMates = MatchingMates.empty();
    }

    public static Member admin(String email, Password password, Nickname nickname) {
        Member member = new Member(email, password, nickname);
        member.roleType = RoleType.ADMIN;
        return member;
    }

    private Member(final Long id,
                   final MemberStatus memberStatus,
                   final String email,
                   final Nickname nickname,
                   final Password password,
                   final MemberPhoto memberPhoto,
                   final RoleType roleType,
                   final LocalDateTime createdAt,
                   final LocalDateTime updatedAt,
                   final MatchingMates matchingMates) {
        this.id = id;
        this.memberStatus = memberStatus;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.memberPhoto = memberPhoto;
        this.roleType = roleType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.matchingMates = matchingMates;
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

    public void leave() {
        this.memberStatus = MemberStatus.DELETED;
        this.email = null;
        this.nickname = null;
        this.password = null;
        this.memberPhoto = null;
        this.matchingMates = null;
    }

    void updateNickname(final Nickname nickname) {
        this.nickname = nickname;
    }

    void updatePassword(final Password password) {
        this.password = password;
    }

    void updateMemberPhoto(final MemberPhoto memberPhoto) {
        this.memberPhoto = memberPhoto;
    }
}