package idorm.idormServer.matching.service;

import idorm.idormServer.exception.CustomException;
import idorm.idormServer.matching.domain.MatchingInfo;
import idorm.idormServer.matching.dto.MatchingInfoRequest;
import idorm.idormServer.matching.repository.MatchingInfoRepository;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
            MatchingInfo savedMatchingInfo = matchingInfoRepository.save(matchingInfo);
            savedMatchingInfo.getMember().updateDormCategory(savedMatchingInfo.getDormCategory());
            return savedMatchingInfo;
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
            matchingInfo.getMember().updateDormCategory(null);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 매칭정보 데이터 삭제 |
     * 회원 탈퇴 시 사용한다. |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteData(Member member) {
        try {
            for (MatchingInfo matchingInfo : member.getAllMatchingInfo()) {
                matchingInfo.deleteData();
            }
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
    public void updateMatchingInfo(MatchingInfo updateMatchingInfo, MatchingInfoRequest request) {

        try {
            updateMatchingInfo.updateMatchingInfo(request);
            updateMatchingInfo.getMember().updateDormCategory(updateMatchingInfo.getDormCategory());
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 매칭인포 단건 조회 |
     * 404(MATCHINGINFO_NOT_FOUND)
     */
//    public MatchingInfo findById(Long matchingInfoId) {
//
//        return matchingInfoRepository.findByIdAndIsDeletedIsFalse(matchingInfoId)
//                .orElseThrow(() -> new CustomException(null, MATCHINGINFO_NOT_FOUND));
//    }

    /**
     * 회원으로 매칭정보 조회 Optional |
     */
    public Optional<MatchingInfo> findByMemberOp(Member member) {
        return matchingInfoRepository.findByMemberIdAndIsDeletedIsFalse(member.getId());
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
    public void validateMatchingInfoIsPublic(MatchingInfo matchingInfo) {

        if (matchingInfo.getIsMatchingInfoPublic())
            throw new CustomException(null, ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC);
    }

    /**
     * MBTI 요청 검증 |
     * 400(MBTI_CHARACTER_INVALID)
     */
    public String validateMBTI(String mbti) {

        if (mbti == null || mbti.length() == 0)
            return null;

        if (mbti.length() < 4)
            throw new CustomException(null, MBTI_LENGTH_INVALID);
        if (mbti.charAt(0) != 'E' && mbti.charAt(0) != 'I')
            throw new CustomException(null, MBTI_CHARACTER_INVALID);
        if (mbti.charAt(1) != 'S' && mbti.charAt(1) != 'N')
            throw new CustomException(null, MBTI_CHARACTER_INVALID);
        if (mbti.charAt(2) != 'T' && mbti.charAt(2) != 'F')
            throw new CustomException(null, MBTI_CHARACTER_INVALID);
        if (mbti.charAt(3) != 'J' && mbti.charAt(3) != 'P')
            throw new CustomException(null, MBTI_CHARACTER_INVALID);

        return mbti.toUpperCase();
    }
}
