package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.entity.Team;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>, TeamCustomRepository {

    Optional<Team> findByMembersId(Long memberId);
}