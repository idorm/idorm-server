package idorm.idormServer.member.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.photo.domain.Photo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity implements UserDetails{

    @Id
    @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String email;
    private String password;
    private String nickname; // 커뮤니티 게시글에선 익명/닉네임 여부 선택 가능, 댓글에선 전부 익명1,2,3

    private Boolean isLeft; // default는 false, 회원 탈퇴 시 true

    /**
     * 연관관계 매핑
     */
    @OneToOne(mappedBy = "member")
    private MatchingInfo matchingInfo;

    @OneToOne(mappedBy = "member")
    private Photo photo; // 멤버의 프로필 사진

    /**
     * security code
     */
    // 해당 USER의 권한을 return 하는 곳

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

        // 우리 사이트에서 1년동안 회원이 로그인을 안하면 휴면계정으로 하기로 했다면
        // return true;
    }

    /**
     * security code end
     */

    @Builder
    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.isLeft = false;
        this.roles.add("ROLE_USER");
    }

    /**
     * 비지니스 로직
     */
    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateIsLeft() {
        this.isLeft = true; // 탈퇴 처리
    }

  public void updateMatchingInfo(MatchingInfo matchingInfo) {
      this.matchingInfo = matchingInfo;
  }

  public void deleteMatchingInfo() {
        this.matchingInfo = null;
  }

}