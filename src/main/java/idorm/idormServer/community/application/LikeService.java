package idorm.idormServer.community.application;

import org.springframework.stereotype.Service;

import idorm.idormServer.community.application.port.in.LikeUseCase;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService implements LikeUseCase {
	@Override
	public void findLikedPostsByMember(Member member) {

	}

	@Override
	public void save(Member member) {

	}

	@Override
	public void delete(Member member) {

	}
}
