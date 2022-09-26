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
    private final MemberService memberService;

    /**
     * MatchingInfo 저장 |
     * 매칭정보를 저장한다. 멤버 연관관계를 매칭해야한다.
     */
    @Transactional
    public MatchingInfo save(MatchingInfoDefaultRequestDto requestDto, Member member) {

        log.info("IN PROGRESS | MatchingInfo 저장 At " + LocalDateTime.now() + " | " + member.getEmail());

        try {
            MatchingInfo matchingInfo = requestDto.toEntity(member);

            matchingInfoRepository.save(matchingInfo);
            memberService.updateMatchingInfo(member, matchingInfo);

            log.info("COMPLETE | MatchingInfo 저장 At " + LocalDateTime.now() + " | " + matchingInfo.getMember().getEmail());
            return matchingInfo;
        } catch(Exception e) {
            throw new InternalServerErrorException("MatchingInfo save 중 서버 에러 발생", e);
        }
    }

    /**
     * MatchingInfo 매칭이미지 공개여부 변경 |
     * true일 경우만 매칭 시 조회한다.
     */
    @Transactional
    public MatchingInfo updateMatchingInfoIsPublic(Member member) {

        log.info("IN PROGRESS | MatchingInfo 매칭이미지 공개여부 변경 At " + LocalDateTime.now() + " | " + member.getEmail());

        try {
            Optional<MatchingInfo> foundMatchingInfo = matchingInfoRepository.findByMemberId(member.getId());

            if(foundMatchingInfo.isEmpty()) {
                throw new ConflictException("등록된 매칭정보가 없습니다.");
            }

            foundMatchingInfo.get().updateIsMatchingInfoPublic();

            matchingInfoRepository.save(foundMatchingInfo.get());
            memberService.updateMatchingInfo(member, foundMatchingInfo.get());

            log.info("COMPLETE | MatchingInfo 매칭이미지 공개여부 변경 At " + LocalDateTime.now() + " | " + member.getEmail());
            return foundMatchingInfo.get();
        } catch(Exception e) {
            throw new InternalServerErrorException("MatchingInfo 매칭이미지 공개 여부 변경 중 서버 에러 발생", e);
        }
    }

    /**
     * MatchingInfo 단건 조회 |
     */
    public MatchingInfo findById(Long matchingInfoId) {

        log.info("IN PROGRESS | MatchingInfo 단건 조회 At " + LocalDateTime.now());

        Optional<MatchingInfo> foundMatchingInfo = matchingInfoRepository.findById(matchingInfoId);

        try {
            if(foundMatchingInfo.isEmpty()) {
                throw new NotFoundException("조회할 매칭정보가 존재하지 않습니다.");
            }

            log.info("COMPLETE | MatchingInfo 단건 조회 At " + LocalDateTime.now());
            return foundMatchingInfo.get();
        } catch (Exception e) {
            throw new InternalServerErrorException("MatchingInfo 단건 조회 중 서버 에러 발생", e);
        }
    }

    public Long findByMemberId(Long memberId) {
        log.info("IN PROGRESS | MatchingInfo Member 식별자로 단건 조회 At " + LocalDateTime.now());

        Optional<Long> matchingInfoId = matchingInfoRepository.findMatchingInfoIdByMemberId(memberId);

        try {
            if(matchingInfoId.isEmpty()) {
                throw new NotFoundException("조회할 매칭정보가 존재하지 않습니다.");
            }
            log.info("COMPLETE | MatchingInfo Member 식별자로 단건 조회 At " + LocalDateTime.now());
            return matchingInfoId.get();
        } catch (Exception e) {
            throw new InternalServerErrorException("MatchingInfo Member 식별자로 단건 조회 중 서버 에러 발생", e);
        }
    }

    /**
     * MatchingInfo 전체 조회 |
     */
    public List<MatchingInfo> findAll() {

        log.info("IN PROGRESS | MatchingInfo 전체 조회 At " + LocalDateTime.now());

        try {
            List<MatchingInfo> foundAllMatchingInfos = matchingInfoRepository.findAll();

            if(foundAllMatchingInfos.isEmpty()) {
                throw new NotFoundException("조회할 매칭정보가 존재하지 않습니다.");
            }

            log.info("COMPLETE | MatchingInfo 전체 조회 At " + LocalDateTime.now() + " | MatchingInfo 수: " + foundAllMatchingInfos.size());
            return foundAllMatchingInfos;
        } catch (Exception e) {
            throw new InternalServerErrorException("MatchingInfo 전체 조회 중 서버 에러 발생", e);
        }
    }

    /**
     * MatchingInfo 삭제 |
     */
    @Transactional
    public void deleteMatchingInfo(Long matchingInfoId) {

        log.info("IN PROGRESS | MatchingInfo 삭제 At " + LocalDateTime.now());

        Optional<MatchingInfo> foundMatchingInfo = matchingInfoRepository.findById(matchingInfoId);
        Member member = foundMatchingInfo.get().getMember();

        if(foundMatchingInfo.isEmpty()) {
            throw new NotFoundException("삭제할 매칭정보가 존재하지 않습니다.");
        }

        try {
            matchingInfoRepository.delete(foundMatchingInfo.get());
            memberService.deleteMatchingInfo(member);
        } catch(Exception e) {
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
        } catch (Exception e) {
            throw new InternalServerErrorException("MatchingInfo 수정 중 서버 에러 발생", e);
        }

        log.info("COMPLETE | MatchingInfo 수정 At " + LocalDateTime.now());
    }

    /**
     * MatchingInfo 매칭 이미지 공개여부 수정 |
     */
    @Transactional
    public void updateMatchingInfoIsPublic(long matchingInfoId) {

        log.info("IN PROGRESS | MatchingInfo 매칭 이미지 공개여부 수정 At " + LocalDateTime.now());

        Optional<MatchingInfo> matchingInfo = matchingInfoRepository.findById(matchingInfoId);



        log.info("COMPLETE | MatchingInfo 매칭 이미지 공개여부 수정 At " + LocalDateTime.now());
    }

}
