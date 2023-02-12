package idorm.idormServer.matching.service;

import idorm.idormServer.exception.CustomException;
import idorm.idormServer.matching.dto.MatchingFilteredMatchingInfoRequestDto;
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

    public List<MatchingInfo> findMatchingMembers(Member member) {

        List<MatchingInfo> foundMatchingInfos = null;
        MatchingInfo loginMemberMatchingInfo = member.getMatchingInfo();
        
        try {
            foundMatchingInfos = 
                    matchingInfoRepository.findAllByMemberIdNotAndDormCategoryAndJoinPeriodAndGenderAndIsMatchingInfoPublicTrue(
                        member.getId(),
                        loginMemberMatchingInfo.getDormCategory(),
                        loginMemberMatchingInfo.getJoinPeriod(),
                        loginMemberMatchingInfo.getGender()
                    );
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }

        if (foundMatchingInfos.isEmpty()) {
            return null;
        }

        List<Member> dislikedMembers = findDislikedMembers(member);

        Iterator<MatchingInfo> iterator = foundMatchingInfos.iterator();
        
        while (iterator.hasNext()) {
            MatchingInfo matchingInfo = iterator.next();

            for (Member dislikedMember : dislikedMembers) {
                if (matchingInfo.getMember().getId() == dislikedMember.getId()) {
                    iterator.remove();
                }
            }
        }

        return foundMatchingInfos;
    }

    /**
     * Matching 매칭멤버 필터링 조회 |
     * 500(SERVER_ERROR)
     */
    public List<MatchingInfo> findFilteredMatchingMembers(Member member,
                                                          MatchingFilteredMatchingInfoRequestDto request) {

        List<MatchingInfo> foundMatchingInfos = null;

        try {
            foundMatchingInfos = matchingInfoRepository.findFilteredMatchingMembers(
                    member.getId(),
                    request.getDormCategory().getType(),
                    request.getJoinPeriod().getType(),
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
            throw new CustomException(SERVER_ERROR);
        }

        if (foundMatchingInfos.isEmpty()) {
            return null;
        }

        List<Member> dislikedMembers = findDislikedMembers(member);

        Iterator<MatchingInfo> iterator = foundMatchingInfos.iterator();

        while (iterator.hasNext()) {
            MatchingInfo matchingInfo = iterator.next();

            for (Member dislikedMember : dislikedMembers) {
                if (matchingInfo.getMember().getId() == dislikedMember.getId()) {
                    iterator.remove();
                }
            }
        }

        return foundMatchingInfos;
    }


    public List<Member> findLikedMembers(Member member) {
        List<Long> likedMembersId = memberRepository.findlikedMembersById(member.getId());

        if (likedMembersId == null)
            return null;

        List<Member> likedMembers = new ArrayList<>();

        for (Long memberId : likedMembersId) {
            Optional<Member> likedMember = memberRepository.findById(memberId);

            if (likedMember.isEmpty() || !likedMember.get().getMatchingInfo().getIsMatchingInfoPublic()){
                removeLikedMember(member, memberId);
                continue;
            }

            likedMembers.add(likedMember.get());
        }
        return likedMembers;
    }

    public List<Member> findDislikedMembers(Member member) {
        List<Long> dislikedMembersId = memberRepository.findDislikedMembersById(member.getId());

        if (dislikedMembersId == null)
            return null;

        List<Member> dislikedMembers = new ArrayList<>();

        for (Long memberId : dislikedMembersId) {
            Optional<Member> dislikedMember = memberRepository.findById(memberId);

            if (dislikedMember.isEmpty() || !dislikedMember.get().getMatchingInfo().getIsMatchingInfoPublic()){
                removeDislikedMember(member, memberId);
                continue;
            }

            dislikedMembers.add(dislikedMember.get());
        }
        return dislikedMembers;
    }

    /**
     * 좋아요한 멤버 추가 |
     * 409(DUPLICATE_LIKED_MEMBER)
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void addLikedMember(Member loginMember, Long likedMemberId) {

        if (isExistLikedMember(loginMember, likedMemberId)) {
            throw new CustomException(DUPLICATE_LIKED_MEMBER);
        }
        if (isExistDislikedMember(loginMember, likedMemberId)) {
            removeDislikedMember(loginMember, likedMemberId);
        }

        try {
            loginMember.addLikedMember(likedMemberId);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    @Transactional
    public void addDislikedMember(Member loginMember, Long dislikedMemberd) {

        if (isExistDislikedMember(loginMember, dislikedMemberd)) {
            throw new CustomException(DUPLICATE_DISLIKED_MEMBER);
        }
        if (isExistLikedMember(loginMember, dislikedMemberd)) {
            removeLikedMember(loginMember, dislikedMemberd);
        }

        try {
            loginMember.addDislikedMember(dislikedMemberd);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    @Transactional
    public void removeLikedMember(Member loginMember, Long likedMemberId) {
        if (!isExistLikedMember(loginMember, likedMemberId)) {
            throw new CustomException(LIKEDMEMBER_NOT_FOUND);
        }

        try {
            loginMember.removeLikedMember(likedMemberId);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    @Transactional
    public void removeDislikedMember(Member loginMember, Long dislikedMemberId) {
        if (!isExistDislikedMember(loginMember, dislikedMemberId)) {
            throw new CustomException(DISLIKEDMEMBER_NOT_FOUND);
        }

        try {
            loginMember.removeDislikedMember(dislikedMemberId);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    private boolean isExistLikedMember(Member loginMember, Long likedMemberId) {
        int result = memberRepository.isExistLikedMember(loginMember.getId(), likedMemberId);
        return result == 1 ? true : false;
    }

    private boolean isExistDislikedMember(Member loginMember, Long dislikedMemberId) {
        int result = memberRepository.isExistDislikedMember(loginMember.getId(), dislikedMemberId);
        return result == 1 ? true : false;
    }
}
