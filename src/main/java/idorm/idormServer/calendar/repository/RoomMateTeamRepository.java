package idorm.idormServer.calendar.repository;

import idorm.idormServer.calendar.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomMateTeamRepository extends JpaRepository<Team, Long> {
}
