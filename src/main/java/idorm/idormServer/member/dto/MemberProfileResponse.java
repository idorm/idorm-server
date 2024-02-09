package idorm.idormServer.member.dto;

import idorm.idormServer.member.domain.Member;

public record MemberProfileResponse(
        Long memberId,
        String email,
        String nickname,
        String profilePhotoUrl
) {

    public static MemberProfileResponse of(final Member member) {
        return new MemberProfileResponse(member.getId(),
                member.getEmail(),
                member.getNickname().getValue(),
                member.getMemberPhoto().getValue());
    }
}
