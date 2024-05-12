package idorm.idormServer.calendar.application.port.in.dto;

import java.util.List;

import idorm.idormServer.calendar.entity.Team;

public record TeamResponse(
	Long teamId,
	boolean isNeedToConfirmDeleted,
	List<TeamParticipantResponse> members
) {
	public static TeamResponse of(final Team team, final List<TeamParticipantResponse> responses) {
		return new TeamResponse(team.getId(),
			team.getTeamStatus().isAlone(),
			responses);
	}
}