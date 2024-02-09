package idorm.idormServer.calendar.service;

import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.calendar.repository.RoomMateTeamRepository;
import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static idorm.idormServer.common.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomMateTeamService {

    private final RoomMateTeamRepository teamRepository;

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
        return member.getRoomMateTeam();
    }

    /**
     * 팀원으로 팀 조회 |
     * 404(TEAM_NOT_FOUND)
     */
    public Team findByMember(Member member) {
        Team team = member.getRoomMateTeam();
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
            Collections.sort(members, Comparator.comparingInt(Member::getTeamOrder));
            return members;
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 팀원 만석 여부 검증 |
     * 409(CANNOT_REGISTER_TEAM_STATUS_FULL)
     */
    public void validateTeamFull(Team team) {
        if (team.getMemberCount() >= 4)
            throw new CustomException(null, CANNOT_REGISTER_TEAM_STATUS_FULL);
    }

    /**
     * 등록된 팀 존재 여부 검증 |
     * 409(DUPLICATE_TEAM)
     */
    public void validateTeamExistence(Member member) {
        if (member.getRoomMateTeam() != null)
            throw new CustomException(null, DUPLICATE_TEAM);
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

    /**
     * 팀원 접근 여부 검증 |
     * 403(ACCESS_DENIED_TEAM)
     */
    public void validateTeamMember(Team loginMemberTeam, Member deleteMember) {
        if (deleteMember.getRoomMateTeam() == null)
            throw new CustomException(null, ACCESS_DENIED_TEAM);

        if (!loginMemberTeam.equals(deleteMember.getRoomMateTeam()))
            throw new CustomException(null, ACCESS_DENIED_TEAM);
    }
}
