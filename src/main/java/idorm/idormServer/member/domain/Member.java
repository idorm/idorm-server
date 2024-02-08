package idorm.idormServer.member.domain;

import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import idorm.idormServer.matchingMate.domain.FavoriteMates;
import idorm.idormServer.matchingMate.domain.NonFavoriteMates;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

    @Column(nullable = false)
    private String email;

    @Embedded
    private Nickname nickname;

    @Embedded
    private Password password;

    @Embedded
    private MemberPhoto memberPhoto;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('USER', 'ADMIN')")
    private RoleType roleType = RoleType.USER;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Setter
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Embedded
    private FavoriteMates favoriteMates = FavoriteMates.empty();

    @Embedded
    private NonFavoriteMates nonFavoriteMates = NonFavoriteMates.empty();

    @Builder
    public Member(String email, Password password, Nickname nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.memberStatus = MemberStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Member admin(String email, Password password, Nickname nickname) {
        Member member = new Member(email, password, nickname);
        member.roleType = RoleType.ADMIN;
        return member;
    }

    void updateNickname(Nickname nickname) {
        this.nickname = nickname;
    }

    void updatePassword(Password password) {
        this.password = password;
    }

    void updateMemberPhoto(MemberPhoto memberPhoto) {
        this.memberPhoto = memberPhoto;
    }

    public void validateUniqueFavoriteMate(Member targetMember) {
        if (this.favoriteMates.isDuplicated(targetMember)) {
            throw new CustomException(null, ExceptionCode.DUPLICATE_LIKED_MEMBER);
        }
    }

    public void validateUniqueNonFavoriteMate(Member targetMember) {
        if (this.nonFavoriteMates.isDuplicated(targetMember)) {
            throw new CustomException(null, ExceptionCode.DUPLICATE_DISLIKED_MEMBER);
        }
    }
}