package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.entity.Team;

import org.springframework.stereotype.Repository;

@Repository
public interface TeamCustomRepository {

	Team findByMemberId(Long memberId);
}