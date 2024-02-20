package idorm.idormServer.calendar.application.port.in.dto;

import idorm.idormServer.member.domain.Member;

public record TeamCalendarParticipantResponse(
	int order,
	Long memberId,
	String nickname,
	String profilePhotoUrl
) {

	public static TeamCalendarParticipantResponse of(final Member member, final int order) {
		return new TeamCalendarParticipantResponse(
			order,
			member.getId(),
			member.getNickname().getValue(),
			member.getMemberPhoto().getValue()
		);
	}
}
