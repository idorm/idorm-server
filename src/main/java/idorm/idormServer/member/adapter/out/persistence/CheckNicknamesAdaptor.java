package idorm.idormServer.member.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.member.adapter.out.exception.DuplicatedNicknameException;
import idorm.idormServer.member.application.port.out.CheckNicknamesPort;
import idorm.idormServer.member.entity.MemberStatus;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CheckNicknamesAdaptor implements CheckNicknamesPort {

	private final MemberRepository memberRepository;

	@Override
	public void validateUniqueNickname(final String nickname) {
		boolean existsed = memberRepository.existsByNicknameValueAndMemberStatus(nickname, MemberStatus.ACTIVE);
		if (existsed) {
			throw new DuplicatedNicknameException();
		}
	}
}