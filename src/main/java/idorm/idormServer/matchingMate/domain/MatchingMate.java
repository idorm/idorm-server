package idorm.idormServer.matchingMate.domain;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.member.domain.Member;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
public class MatchingMate {

    private Long id;
    private Member member;
    private Member targetMember;

    public MatchingMate(final Member loginMember, final Member targetMember) {
        validateConstructor(loginMember, targetMember);
        this.member = loginMember;
        this.targetMember = targetMember;
    }

    private static void validateConstructor(Member loginMember, Member targetMember) {
        Validator.validateNotNull(List.of(loginMember, targetMember));
    }

    public static MatchingMate forMapper(final Member loginMember, final Member targetMember) {
        return new MatchingMate(loginMember, targetMember);
    }
}