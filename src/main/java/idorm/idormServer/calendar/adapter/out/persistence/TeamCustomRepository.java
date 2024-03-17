package idorm.idormServer.calendar.adapter.out.persistence;

import org.springframework.stereotype.Repository;

import idorm.idormServer.calendar.entity.Team;

@Repository
public interface TeamCustomRepository {
	Team findByMemberId(Long memberId);
}
