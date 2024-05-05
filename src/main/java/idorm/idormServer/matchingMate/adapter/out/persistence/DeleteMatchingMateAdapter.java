package idorm.idormServer.matchingMate.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.matchingMate.application.port.out.DeleteMatchingMatePort;
import idorm.idormServer.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteMatchingMateAdapter implements DeleteMatchingMatePort {

	private final MatchingMateRepository matchingMateRepository;

	@Override
	public void deleteByMemberWithdrawn(Member deletedMember) {
		matchingMateRepository.deleteAllByMatchingInfoForTargetId(deletedMember.getMatchingInfo().getId());
		matchingMateRepository.deleteAllByOwnerId(deletedMember.getId());
	}
}