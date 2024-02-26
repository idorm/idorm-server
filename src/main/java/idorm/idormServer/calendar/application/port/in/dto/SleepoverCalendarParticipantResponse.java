package idorm.idormServer.calendar.application.port.in.dto;

import idorm.idormServer.member.entity.Member;

public record SleepoverCalendarParticipantResponse(
	Long memberId,
	String nickname,
	String profilePhotoUrl
) {

	public static SleepoverCalendarParticipantResponse of(final Member member) {
		return new SleepoverCalendarParticipantResponse(
			member.getId(),
			member.getNickname().getValue(),
			member.getProfilePhotoUrl()
		);
	}
}
