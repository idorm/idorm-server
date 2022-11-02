package idorm.idormServer.member.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.domain.PostLikedMember;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.photo.domain.Photo;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
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

    private String email;
    private String password;
    private String nickname;

    /**
     * 연관관계 매핑
     */
    @OneToOne(mappedBy = "member")
    private MatchingInfo matchingInfo; // 매칭 정보

    @OneToOne(mappedBy = "member")
    private Photo photo; // 프로필 사진

    @OneToMany(mappedBy = "member", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>(); // 작성한 게시글

    @OneToMany(mappedBy = "member")
    private List<PostLikedMember> postLikedMembers = new ArrayList<>(); // 멤버가 공감한 게시글들

//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "comment_id")
//    private List<Comment> comments = new ArrayList<>(); // 작성한 댓글들
//
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sub_comment_id")
//    private List<SubComment> subComments = new ArrayList<>(); // 작성한 대댓글들

    /**
     * security code
     */

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

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

    /**
     * security code end
     */

    @Builder
    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.roles.add("ROLE_USER");
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateMatchingInfo(MatchingInfo matchingInfo) {
        this.matchingInfo = matchingInfo;
    }

    public void deleteMatchingInfo() {
        this.matchingInfo = null;
    }

    public void updatePhoto(Photo photo) {
        this.photo = photo;
    }
}