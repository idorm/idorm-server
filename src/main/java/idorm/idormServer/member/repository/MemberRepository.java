package idorm.idormServer.member.repository;

import idorm.idormServer.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "SELECT member_id " +
            "FROM member m " +
            "WHERE m.email = :email", nativeQuery = true)
    Optional<Long> findMemberIdByEmail(@Param("email") String email);

    Optional<Member> findByNickname(String nickname);
}
