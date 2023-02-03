package idorm.idormServer.member.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.domain.PostLikedMember;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.photo.domain.Photo;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
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
    private LocalDate nicknameUpdatedAt;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private MatchingInfo matchingInfo;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Photo photo;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PostLikedMember> postLikedMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

    @Builder
    public Member(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.roles.add("ROLE_USER");
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePhoto(Photo photo) {
        this.photo = photo;
    }

    public void updateNicknameUpdatedAt(LocalDate updatedAt) {
        this.nicknameUpdatedAt = updatedAt;
    }

    public void setMatchingInfo(MatchingInfo matchingInfo) {
        this.matchingInfo = matchingInfo;
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