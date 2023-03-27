package idorm.idormServer.auth;

import idorm.idormServer.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 사용자 정보 존재 여부 확인 |
     * 404(MEMBER_NOT_FOUND)
     */
    @Override
    public UserDetails loadUserByUsername(String username) {

        return memberRepository.findByIdAndIsDeletedIsFalse(Long.parseLong(username))
                .orElseThrow(() -> new UsernameNotFoundException("등록된 멤버가 없습니다."));
    }
}