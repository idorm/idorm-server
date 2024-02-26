package idorm.idormServer.member.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.member.application.port.out.SaveMemberPort;
import idorm.idormServer.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SaveMemberAdaptor implements SaveMemberPort {

	private final MemberRepository memberRepository;

	@Override
	public void save(final Member member) {
		memberRepository.save(member);
	}
}
