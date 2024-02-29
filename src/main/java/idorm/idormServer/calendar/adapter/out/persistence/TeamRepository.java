package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.entity.Team;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

  Optional<Team> findByMembersId(Long memberId);
}
