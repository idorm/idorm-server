package idorm.idormServer.member.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.member.application.port.out.SaveMemberPort;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SaveMemberAdaptor implements SaveMemberPort {

	private final MemberRepository memberRepository;
	private final MemberMapper memberMapper;

	@Override
	public void save(final Member member) {
		memberRepository.save(memberMapper.toEntity(member));
	}
}
