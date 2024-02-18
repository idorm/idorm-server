package idorm.idormServer.member.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.member.adapter.out.exception.DuplicatedNicknameException;
import idorm.idormServer.member.application.port.out.CheckNicknamesPort;
import idorm.idormServer.member.domain.Nickname;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CheckNicknamesAdaptor implements CheckNicknamesPort {

	private final MemberRepository memberRepository;
	private final NicknameMapper nicknameMapper;

	@Override
	public void validateUniqueNickname(final Nickname nickname) {
		boolean existsed = memberRepository.existsMemberByNicknameValueAndMemberStatusIsActive(
			nicknameMapper.toEntity(nickname));

		if (existsed) {
			throw new DuplicatedNicknameException();
		}
	}
}