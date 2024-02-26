package idorm.idormServer.member.application.port.in.dto;

import idorm.idormServer.member.entity.Member;

public record MemberInfoResponse(
	Long memberId,
	String email,
	String nickname,
	String profilePhotoUrl
) {

	public static MemberInfoResponse of(final Member member) {
		return new MemberInfoResponse(member.getId(),
			member.getEmail(),
			member.getNickname().getValue(),
			member.getProfilePhotoUrl());
	}
}