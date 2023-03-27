package idorm.idormServer.matching.service;

import idorm.idormServer.exception.CustomException;
import idorm.idormServer.matching.dto.MatchingFilteredMatchingInfoRequestDto;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.matchingInfo.domain.JoinPeriod;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.repository.MatchingInfoRepository;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static idorm.idormServer.exception.ExceptionCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingInfoRepository matchingInfoRepository;
    private final MemberRepository memberRepository;

    /**
     * 좋아요한 멤버 추가 |
     * 409(DUPLICATE_LIKED_MEMBER)
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void addLikedMember(Member loginMember, Long likedMemberId) {

        if (isExistLikedMember(loginMember, likedMemberId))
            throw new CustomException(null, DUPLICATE_LIKED_MEMBER);

        if (isExistDislikedMember(loginMember, likedMemberId))
            removeDislikedMember(loginMember, likedMemberId);

        try {
            loginMember.addLikedMember(likedMemberId);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 싫어요한 멤버 추가 |
     * 409(DUPLICATE_DISLIKED_MEMBER)
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void addDislikedMember(Member loginMember, Long dislikedMemberd) {

        if (isExistDislikedMember(loginMember, dislikedMemberd))
            throw new CustomException(null, DUPLICATE_DISLIKED_MEMBER);

        if (isExistLikedMember(loginMember, dislikedMemberd))
            removeLikedMember(loginMember, dislikedMemberd);

        try {
            loginMember.addDislikedMember(dislikedMemberd);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 좋아요한 회원 삭제 |
     * 404(LIKEDMEMBER_NOT_FOUND)
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void removeLikedMember(Member loginMember, Long likedMemberId) {
        if (!isExistLikedMember(loginMember, likedMemberId))
            throw new CustomException(null, LIKEDMEMBER_NOT_FOUND);

        try {
            loginMember.removeLikedMember(likedMemberId);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 싫어요한 회원 삭제 |
     * 404(DISLIKEDMEMBER_NOT_FOUND)
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void removeDislikedMember(Member loginMember, Long dislikedMemberId) {
        if (!isExistDislikedMember(loginMember, dislikedMemberId))
            throw new CustomException(null, DISLIKEDMEMBER_NOT_FOUND);

        try {
            loginMember.removeDislikedMember(dislikedMemberId);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 매칭 회원 전체 조회 |
     * 500(SERVER_ERROR)
     */
    public List<MatchingInfo> findMatchingMembers(Member member) {

        List<MatchingInfo> foundMatchingInfos = null;
        MatchingInfo loginMemberMatchingInfo = member.getMatchingInfo();
        
        try {
            foundMatchingInfos = 
                    matchingInfoRepository.findAllByMemberIdNotAndDormCategoryAndJoinPeriodAndGenderAndIsMatchingInfoPublicTrueAndIsDeletedIsFalse(
                        member.getId(),
                        loginMemberMatchingInfo.getDormCategory(),
                        loginMemberMatchingInfo.getJoinPeriod(),
                        loginMemberMatchingInfo.getGender()
                    );
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }

        if (foundMatchingInfos.isEmpty()) {
            return null;
        }

        List<Member> dislikedMembers = findDislikedMembers(member);

        Iterator<MatchingInfo> iterator = foundMatchingInfos.iterator();
        
        while (iterator.hasNext()) {
            MatchingInfo matchingInfo = iterator.next();

            for (Member dislikedMember : dislikedMembers) {
                if (matchingInfo.getMember().getIsDeleted())
                    iterator.remove();
                else if (matchingInfo.getMember().getId() == dislikedMember.getId())
                    iterator.remove();
            }
        }

        return foundMatchingInfos;
    }

    /**
     * 매칭 회원 필터링 조회 |
     * 500(SERVER_ERROR)
     */
    public List<MatchingInfo> findFilteredMatchingMembers(Member member,
                                                          MatchingFilteredMatchingInfoRequestDto request) {

        List<MatchingInfo> foundMatchingInfos = null;

        try {
            foundMatchingInfos = matchingInfoRepository.findFilteredMatchingMembers(
                    member.getId(),
                    DormCategory.validateType(request.getDormCategory()).getType(),
                    JoinPeriod.validateType(request.getJoinPeriod()).getType(),
                    member.getMatchingInfo().getGender(),
                    request.getIsSnoring(),
                    request.getIsSmoking(),
                    request.getIsGrinding(),
                    request.getIsWearEarphones(),
                    request.getIsAllowedFood(),
                    request.getMinAge(),
                    request.getMaxAge()
            );
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }

        if (foundMatchingInfos.isEmpty()) {
            return null;
        }

        List<Member> dislikedMembers = findDislikedMembers(member);

        Iterator<MatchingInfo> iterator = foundMatchingInfos.iterator();

        while (iterator.hasNext()) {
            MatchingInfo matchingInfo = iterator.next();

            for (Member dislikedMember : dislikedMembers) {

                if (matchingInfo.getMember().getIsDeleted())
                    iterator.remove();
                else if (matchingInfo.getMember().getId() == dislikedMember.getId())
                    iterator.remove();
            }
        }

        return foundMatchingInfos;
    }

    /**
     * 좋아요한 회원 전체 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Member> findLikedMembers(Member member) {
        List<Long> likedMembersId = null;

        try {
            likedMembersId = memberRepository.findlikedMembersByLoginMemberId(member.getId());
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }

        if (likedMembersId == null)
            return null;

        List<Member> likedMembers = new ArrayList<>();

        for (Long memberId : likedMembersId) {
            Optional<Member> likedMember = null;

            try {
                likedMember = memberRepository.findByIdAndIsDeletedIsFalse(memberId);
            } catch (RuntimeException e) {
                throw new CustomException(e, SERVER_ERROR);
            }

            if (likedMember.isEmpty() || !likedMember.get().getMatchingInfo().getIsMatchingInfoPublic())
                removeLikedMember(member, memberId);
            else
                likedMembers.add(likedMember.get());

        }
        return likedMembers;
    }

    /**
     * 싫어요한 회원 전체 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Member> findDislikedMembers(Member member) {
        List<Long> dislikedMembersId = null;

        try {
            dislikedMembersId = memberRepository.findDislikedMembersByLoginMemberId(member.getId());
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }

        if (dislikedMembersId == null)
            return null;

        List<Member> dislikedMembers = new ArrayList<>();

        for (Long memberId : dislikedMembersId) {
            Optional<Member> dislikedMember = null;

            try {
                dislikedMember = memberRepository.findByIdAndIsDeletedIsFalse(memberId);
            } catch (RuntimeException e) {
                throw new CustomException(e, SERVER_ERROR);
            }

            if (dislikedMember.isEmpty() || !dislikedMember.get().getMatchingInfo().getIsMatchingInfoPublic())
                removeDislikedMember(member, memberId);
            else
                dislikedMembers.add(dislikedMember.get());

        }
        return dislikedMembers;
    }

    /**
     * 매칭 좋아요 여부 확인 |
     */
    private boolean isExistLikedMember(Member loginMember, Long likedMemberId) {
        int result = memberRepository.isExistLikedMember(loginMember.getId(), likedMemberId);
        return result == 1 ? true : false;
    }

    /**
     * 매칭 싫어요 여부 확인 |
     */
    private boolean isExistDislikedMember(Member loginMember, Long dislikedMemberId) {
        int result = memberRepository.isExistDislikedMember(loginMember.getId(), dislikedMemberId);
        return result == 1 ? true : false;
    }
}
