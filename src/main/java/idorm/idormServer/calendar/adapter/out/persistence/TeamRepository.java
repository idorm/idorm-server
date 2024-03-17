package idorm.idormServer.calendar.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import idorm.idormServer.calendar.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>, TeamCustomRepository {
}
