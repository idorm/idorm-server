package idorm.idormServer.calendar.application.port.in.dto;

import idorm.idormServer.member.entity.Member;

public record TeamParticipantResponse(
	int order,
	Long memberId,
	String nickname,
	String profilePhotoUrl,
	Boolean sleepoverYn
) {

	public static TeamParticipantResponse of(final Member member, final int order,
		final Boolean sleepoverYn) {
		return new TeamParticipantResponse(order,
			member.getId(),
			member.getNickname().getValue(),
			member.getProfilePhotoUrl(),
			sleepoverYn);
	}
}
