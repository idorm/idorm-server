package idorm.idormServer.member.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.domain.PostLikedMember;
import idorm.idormServer.email.domain.Email;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.photo.domain.MemberPhoto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity implements UserDetails {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    private String nickname;
    private LocalDateTime nicknameUpdatedAt;
    private String fcmToken;
    private LocalDate fcmTokenUpdatedAt;
    private Integer reportedCount;

    @OneToMany(mappedBy = "member")
    private List<Email> emails = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MatchingInfo> matchingInfos = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberPhoto> memberPhotos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "liked_members",
            joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "liked_member")
    private List<Long> likedMembers = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "disliked_members",
            joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "disliked_member")
    private List<Long> dislikedMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PostLikedMember> postLikedMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

    @Builder
    public Member(Email email, String password, String nickname) {
        this.emails.add(email);
        this.password = password;
        this.nickname = nickname;
        this.nicknameUpdatedAt = null;
        this.reportedCount = 0;
        this.roles.add("ROLE_USER");

        this.setIsDeleted(false);
    }

    public Email getEmail() {
        int size = this.emails.size();
        if (size < 1)
            return null;
        Email email = this.emails.get(size - 1);
        if (email.getIsDeleted())
            return null;
        return email;
    }

    public MatchingInfo getMatchingInfo() {
        int matchingInfoSize = this.matchingInfos.size();
        if (matchingInfoSize == 0)
            return null;

        MatchingInfo matchingInfo = this.matchingInfos.get(matchingInfoSize - 1);
        if (matchingInfo.getIsDeleted())
            return null;
        return matchingInfo;
    }

    // 회원 탈퇴 시 사용
    public List<MatchingInfo> getAllMatchingInfo() {
        return this.matchingInfos;
    }

    public MemberPhoto getMemberPhoto() {
        int profilePhotoSize = this.memberPhotos.size();
        if (profilePhotoSize == 0)
            return null;

        MemberPhoto memberPhoto = this.memberPhotos.get(profilePhotoSize - 1);
        if (memberPhoto.getIsDeleted())
            return null;
        return memberPhoto;
    }

    /**
     * 회원 탈퇴 시 사용
     */
    public List<MemberPhoto> getAllMemberPhoto() {
        return this.memberPhotos;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
        this.nicknameUpdatedAt = LocalDateTime.now();
    }

    public void addLikedMember(Long memberId) {
        this.likedMembers.add(memberId);
    }

    public void removeLikedMember(Long memberId) {
        this.likedMembers.remove(memberId);
    }

    public void addDislikedMember(Long memberId) {
        this.dislikedMembers.add(memberId);
    }

    public void removeDislikedMember(Long memberId) {
        this.dislikedMembers.remove(memberId);
    }

    public void updateFcmToken(String token) {
        this.fcmToken = token;
        this.fcmTokenUpdatedAt = LocalDate.now();
    }

    public void deleteFcmToken() {
        this.fcmToken = null;
        this.fcmTokenUpdatedAt = LocalDate.now();
    }

    public void incrementreportedCount() {
        this.reportedCount += 1;
    }

    public void delete() {
        this.fcmToken = null;
        this.fcmTokenUpdatedAt = null;
        this.nickname = null;
        this.nicknameUpdatedAt = null;
        this.password = null;
        this.reportedCount =  null;
        this.setIsDeleted(true);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}