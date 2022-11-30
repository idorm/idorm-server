package idorm.idormServer.matchingInfo.service;

import idorm.idormServer.exceptions.http.ConflictException;
import idorm.idormServer.exceptions.http.InternalServerErrorException;
import idorm.idormServer.exceptions.http.NotFoundException;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.dto.MatchingInfoDefaultRequestDto;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import idorm.idormServer.matchingInfo.repository.MatchingInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingInfoService {

    private final MatchingInfoRepository matchingInfoRepository;

    /**
     * MatchingInfo 저장 |
     */
    @Transactional
    public MatchingInfo save(MatchingInfoDefaultRequestDto requestDto, Member member) {

        log.info("IN PROGRESS | MatchingInfo 저장 At " + LocalDateTime.now() + " | " + member.getEmail());

        MatchingInfo matchingInfo = requestDto.toEntity(member);
        matchingInfoRepository.save(matchingInfo);

        log.info("COMPLETE | MatchingInfo 저장 At " + LocalDateTime.now() + " | " + matchingInfo.getMember().getEmail());
        return matchingInfo;
    }

    /**
     * MatchingInfo 매칭이미지 공개여부 변경 |
     * true일 경우만 매칭 시 조회한다.
     */
    @Transactional
    public MatchingInfo updateMatchingInfoIsPublic(Member member, Boolean isMatchingInfoPublic) {

        log.info("IN PROGRESS | MatchingInfo 매칭이미지 공개여부 변경 At " + LocalDateTime.now() + " | " + member.getEmail());

        Optional<MatchingInfo> foundMatchingInfo = matchingInfoRepository.findByMemberId(member.getId());

        if(foundMatchingInfo.isEmpty()) {
            throw new ConflictException("등록된 매칭정보가 없습니다.");
        }

        foundMatchingInfo.get().updateIsMatchingInfoPublic(isMatchingInfoPublic);
        matchingInfoRepository.save(foundMatchingInfo.get());

        log.info("COMPLETE | MatchingInfo 매칭이미지 공개여부 변경 At " + LocalDateTime.now() + " | " + member.getEmail());
        return foundMatchingInfo.get();
    }

    /**
     * MatchingInfo 단건 조회 |
     * 매칭인포 식별자로 매칭인포를 단건 조회한다. 저장된 매칭정보가 없다면 404(Not Found)를 던진다.
     */
    public MatchingInfo findById(Long matchingInfoId) {

        log.info("IN PROGRESS | MatchingInfo 단건 조회 At " + LocalDateTime.now());

        Optional<MatchingInfo> foundMatchingInfo = matchingInfoRepository.findById(matchingInfoId);

        if(foundMatchingInfo.isEmpty()) {
            throw new NotFoundException("조회할 매칭정보가 존재하지 않습니다.");
        }

        log.info("COMPLETE | MatchingInfo 단건 조회 At " + LocalDateTime.now());
        return foundMatchingInfo.get();
    }

    /**
     * MatchingInfo Optional 단건 조회 |
     */
    public Optional<MatchingInfo> findOpByMemberId(Long memberId) {
        log.info("IN PROGRESS | MatchingInfo Optional 단건 조회 At " + LocalDateTime.now());

        Optional<MatchingInfo> foundMatchingInfo = matchingInfoRepository.findByMemberId(memberId);

        log.info("COMPLETE | MatchingInfo Optional 단건 조회 At " + LocalDateTime.now());
        return foundMatchingInfo;
    }

    /**
     * MatchingInfo 단건 조회 |
     * 멤버 식별자로 매칭인포를 단건 조회한다. 저장된 매칭정보가 없다면 404(Not Found)를 던진다.
     */
    public Long findByMemberId(Long memberId) {
        log.info("IN PROGRESS | MatchingInfo Member 식별자로 단건 조회 At " + LocalDateTime.now());

        Optional<Long> matchingInfoId = matchingInfoRepository.findMatchingInfoIdByMemberId(memberId); // query did not return a unique result: 2

        if(matchingInfoId.isEmpty()) {
            throw new NotFoundException("조회할 매칭정보가 존재하지 않습니다.");
        }

        log.info("COMPLETE | MatchingInfo Member 식별자로 단건 조회 At " + LocalDateTime.now());
        return matchingInfoId.get();
    }

    /**
     * MatchingInfo 삭제 |
     */
    @Transactional
    public void deleteMatchingInfo(MatchingInfo matchingInfo) {

        log.info("IN PROGRESS | MatchingInfo 삭제 At " + LocalDateTime.now());

        try {
            matchingInfoRepository.delete(matchingInfo);
        } catch(InternalServerErrorException e) {
            throw new InternalServerErrorException("MatchingInfo 삭제 중 서버 에러 발생", e);
        }
        log.info("COMPLETE | MatchingInfo 삭제 At " + LocalDateTime.now());
    }

    /**
     * MatchingInfo 수정 |
     */
    @Transactional
    public void updateMatchingInfo(Long matchingInfoId, MatchingInfoDefaultRequestDto requestDto) {

        log.info("IN PROGRESS | MatchingInfo 수정 At " + LocalDateTime.now());

        Optional<MatchingInfo> updateMatchingInfo = matchingInfoRepository.findById(matchingInfoId);

        if(updateMatchingInfo.isEmpty()) {
            throw new NotFoundException("등록된 매칭정보가 존재하지 않습니다.");
        }

        try {
            updateMatchingInfo.get().updateMatchingInfo(requestDto);
            matchingInfoRepository.save(updateMatchingInfo.get());
        } catch (InternalServerErrorException e) {
            throw new InternalServerErrorException("MatchingInfo 수정 중 서버 에러 발생", e);
        }

        log.info("COMPLETE | MatchingInfo 수정 At " + LocalDateTime.now());
    }
}
