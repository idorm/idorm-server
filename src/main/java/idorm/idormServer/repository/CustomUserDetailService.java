package idorm.idormServer.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
    스프링 시큐리티에서 유저를 찾는 메소드를 제공하는 UserDetails를 추가
    UserDetailsService의 loadUserByUsername메소드를 오버라이딩 하여 유저를 찾는 방법을 직접 지정한다.
    반환타입은 UserDetails타입
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws IllegalStateException {
        return memberRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
    }

}
