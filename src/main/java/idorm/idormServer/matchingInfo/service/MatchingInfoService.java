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
     * MatchingInfo DB에 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void saveMatchingInfo(MatchingInfo matchingInfo) {
        try {
            matchingInfoRepository.save(matchingInfo);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * MatchingInfo 생성 |
     */
    @Transactional
    public MatchingInfo createMatchingInfo(MatchingInfoDefaultRequestDto requestDto, Member member) {

        MatchingInfo matchingInfo = requestDto.toEntity(member);
        saveMatchingInfo(matchingInfo);

        return matchingInfo;
    }

    /**
     * MatchingInfo 매칭이미지 공개여부 변경 |
     * 404(MATCHING_INFO_NOT_FOUND)
     */
    @Transactional
    public void updateMatchingInfoIsPublic(MatchingInfo updateMatchingInfo, boolean isMatchingInfoPublic) {

        try {
            updateMatchingInfo.updateIsMatchingInfoPublic(isMatchingInfoPublic);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * MatchingInfo 단건 조회 |
     * 404(MATCHING_INFO_NOT_FOUND)
     */
    public MatchingInfo findById(Long matchingInfoId) {

        MatchingInfo foundMatchingInfo = matchingInfoRepository.findById(matchingInfoId)
                .orElseThrow(() -> new CustomException(MATCHING_INFO_NOT_FOUND));
        return foundMatchingInfo;
    }

    /**
     * MatchingInfo 단건 조회 |
     * 404(MATCHING_INFO_NOT_FOUND)
     */
    public MatchingInfo findByMemberId(Long memberId) {

        MatchingInfo foundMatchingInfo = matchingInfoRepository.findByMemberId(memberId).orElseThrow(
                () -> new CustomException(MATCHING_INFO_NOT_FOUND));

        return foundMatchingInfo;
    }

    /**
     * MatchingInfo 삭제 |
     */
    @Transactional
    public void deleteMatchingInfo(MatchingInfo matchingInfo) {

        try {
            matchingInfoRepository.delete(matchingInfo);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * MatchingInfo 수정 |
     */
    @Transactional
    public void updateMatchingInfo(MatchingInfo updateMatchingInfo, MatchingInfoDefaultRequestDto request) {

        try {
            updateMatchingInfo.updateMatchingInfo(request);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }
}
