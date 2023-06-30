package idorm.idormServer.calendar.service;

import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.calendar.repository.TeamRepository;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    /**
     * DB에 팀 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public Team save(Team team) {
        try {
            return teamRepository.save(team);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 팀 생성 |
     */
    @Transactional
    public Team create(Member member) {
        Team team = Team.builder()
                .member(member)
                .build();
        return this.save(team);
    }

    /**
     * 팀 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void delete(Team team) {
        try {
            team.delete();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 팀원 추가 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void addMember(Team team, Member member) {
        try {
            team.addMember(member);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 팀원 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void removeMember(Team team, Member member) {
        try {
            int deletedMemberTeamOrder = member.getTeamOrder();
            boolean result = team.removeMember(member);
            if (result) {
                if (team.getMembers().size() < 1) return;

                for (Member remainMember : team.getMembers()) {
                    int remainMemberTeamOrder = remainMember.getTeamOrder();
                    if (remainMemberTeamOrder > deletedMemberTeamOrder)
                        remainMember.updateTeamOrder(team, remainMemberTeamOrder - 1);
                }
            }
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 팀 폭발 여부 수정 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updateIsNeedToConfirmDeleted(Team team) {
        try {
            team.updateIsNeedToConfirmDeleted();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 팀원으로 팀 조회 Optional |
     */
    public Team findByMemberOptional(Member member) {
        return member.getTeam();
    }

    /**
     * 팀원으로 팀 조회 |
     * 500(TEAM_NOT_FOUND)
     */
    public Team findByMember(Member member) {
        Team team = member.getTeam();
        if (team == null)
            throw new CustomException(null, TEAM_NOT_FOUND);
        else
            return team;
    }

    /**
     * 팀원 전체 조회 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public List<Member> findTeamMembers(Team team) {

        try {
            List<Member> members = team.getMembers();
            for (Member member : members) {
                if (member.getIsDeleted())
                    team.removeMember(member);
            }
            return members;
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 팀원 만석 여부 검증 |
     * 409(TEAM_STATUS_FULL)
     */
    public void validateTeamFull(Team team) {
        if (team.getMemberCount() >= 4)
            throw new CustomException(null, TEAM_STATUS_FULL);
    }

    /**
     * 등록된 팀 존재 여부 검증 |
     * 409(DUPLICATE_TEAM)
     */
    public void validateTeamExistence(Member member) {
        if (member.getTeam() != null)
            throw new CustomException(null, DUPLICATE_TEAM);
    }

    /**
     * 등록된 팀 미존재 여부 검증 |
     * 409(TEAM_NOT_FOUND)
     */
    public void validateTeamNotExistence(Member member) {
        if (member.getTeam() == null)
            throw new CustomException(null, TEAM_NOT_FOUND);
    }

    /**
     * 팀 폭발 가능 여부 검증 |
     * 409(CANNOT_EXPLODE_TEAM)
     */
    public void validateReadyToDeleteTeam(Team team) {
        if (!team.getIsNeedToConfirmDeleted())
            throw new CustomException(null, CANNOT_EXPLODE_TEAM);
    }

    /**
     * 팀 폭발 여부 검증 |
     * 400(ILLEGAL_STATEMENT_EXPLODEDTEAM)
     */
    public void validateIsDeletedTeam(Team team) {
        if (team.getIsNeedToConfirmDeleted())
            throw new CustomException(null, ILLEGAL_STATEMENT_EXPLODEDTEAM);
    }
}
