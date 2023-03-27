package idorm.idormServer.matchingInfo.service;

import idorm.idormServer.exception.CustomException;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.dto.MatchingInfoDefaultRequestDto;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.matchingInfo.repository.MatchingInfoRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingInfoService {

    private final MatchingInfoRepository matchingInfoRepository;

    /**
     * 매칭인포 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public MatchingInfo save(MatchingInfo matchingInfo) {
        try {
            return matchingInfoRepository.save(matchingInfo);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 매칭정보 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void delete(MatchingInfo matchingInfo) {

        try {
            matchingInfo.delete();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 매칭인포 공개 여부 수정 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updateMatchingInfoIsPublic(MatchingInfo updateMatchingInfo, Boolean isMatchingInfoPublic) {

        try {
            updateMatchingInfo.updateIsMatchingInfoPublic(isMatchingInfoPublic);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 매칭인포 수정 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updateMatchingInfo(MatchingInfo updateMatchingInfo, MatchingInfoDefaultRequestDto request) {

        try {
            updateMatchingInfo.updateMatchingInfo(request);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 매칭인포 단건 조회 |
     * 404(MATCHINGINFO_NOT_FOUND)
     */
    public MatchingInfo findById(Long matchingInfoId) {

        return matchingInfoRepository.findByIdAndIsDeletedIsFalse(matchingInfoId)
                .orElseThrow(() -> new CustomException(null, MATCHINGINFO_NOT_FOUND));
    }

    /**
     * 회원 식별자로 매칭정보 단건 조회 |
     * 404(MATCHINGINFO_NOT_FOUND)
     */
    public MatchingInfo findByMemberId(Long memberId) {

        return matchingInfoRepository.findByMemberIdAndIsDeletedIsFalse(memberId)
                .orElseThrow(() -> new CustomException(null, MATCHINGINFO_NOT_FOUND));
    }

    /**
     * 매칭정보 공개 여부 확인 |
     * 400(ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC)
     */
    public void validateMatchingInfoIsPublic(Member member) {
        if (!member.getMatchingInfo().getIsMatchingInfoPublic())
            throw new CustomException(null, ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC);
    }
}
