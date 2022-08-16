package idorm.idormServer.domain;

import idorm.idormServer.domain.common.BaseEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Data
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

    @OneToOne
    @JoinColumn(name="matchingInfo_id")
    private MatchingInfo matchingInfo;

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

}
