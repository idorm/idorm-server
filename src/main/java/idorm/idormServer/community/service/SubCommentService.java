package idorm.idormServer.community.service;

import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.SubComment;
import idorm.idormServer.community.repository.SubCommentRepository;
import idorm.idormServer.exceptions.http.InternalServerErrorException;
import idorm.idormServer.exceptions.http.NotFoundException;
import idorm.idormServer.exceptions.http.UnauthorizedException;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
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
public class SubCommentService {

    private final SubCommentRepository subCommentRepository;
    private MemberService memberService;

    /**
     * SubComment 저장
     */
    @Transactional
    public SubComment saveSubComment(String content,
                                     Boolean isAnonymous,
                                     Comment comment,
                                     Member member) {
        log.info("IN PROGRESS | SubComment 저장 At " + LocalDateTime.now() + " | Comment 식별자: " + comment.getId());

        try {
            SubComment createdSubComment = SubComment.builder()
                    .content(content)
                    .isAnonymous(isAnonymous)
                    .comment(comment)
                    .member(member)
                    .build();

            SubComment savedSubComment = subCommentRepository.save(createdSubComment);
            log.info("COMPLETE | SubComment 저장 At " + LocalDateTime.now() + " | SubComment 식별자: " + savedSubComment.getId());
            return savedSubComment;
        } catch (Exception e) {
            throw new InternalServerErrorException("SubComment 저장 중 서버 에러 발생", e);
        }
    }

    /**
     * SubComment 단건 조회
     */
    public SubComment findById(Long subCommentId) {
        log.info("IN PROGRESS | SubComment 단건 조회 At " + LocalDateTime.now() + " | 대댓글 식별자: " + subCommentId);
        Optional<SubComment> foundSubComment = subCommentRepository.findById(subCommentId);

        if (foundSubComment.isEmpty()) {
            throw new NotFoundException("조회할 대댓글이 존재하지 않습니다.");
        }

        log.info("COMPLETE | SubComment 단건 조회 At " + LocalDateTime.now() + " | 대댓글 식별자: " + subCommentId);
        return foundSubComment.get();
    }

    /**
     * 로그인한 멤버가 작성한 모든 대댓글들 조회
     */
    public List<SubComment> findSubCommentsByMember(Member member) {
        log.info("IN PROGRESS | SubComment 로그인한 멤버가 작성한 대댓글 조회 At " + LocalDateTime.now() + " | 멤버 식별자: " + member.getId());
        memberService.findById(member.getId());

        try {
            List<SubComment> subCommentsByMemberId = subCommentRepository.findSubCommentsByMemberId(member.getId());
            log.info("COMPLETE | SubComment 로그인한 멤버가 작성한 대댓글 조회 At " + LocalDateTime.now() + " | 멤버 식별자: " + member.getId());
            return subCommentsByMemberId;
        } catch (Exception e) {
            throw new InternalServerErrorException("멤버가 작성한 대댓글 전체 조회 중 서버 에러가 발생했습니다.", e);
        }
    }

    /**
     * SubComment 삭제
     */
    @Transactional
    public void deleteSubComment(Long subCommentId, Member member) {
        log.info("IN PROGRESS | SubComment 삭제 At " + LocalDateTime.now() + " | " + subCommentId);
        SubComment foundSubComment = findById(subCommentId);

        if (foundSubComment.getMember().getId() != member.getId()) {
            throw new UnauthorizedException("본인이 작성한 대댓글만 삭제할 수 있습니다.");
        }

        try {
            foundSubComment.deleteSubComment();
            subCommentRepository.save(foundSubComment);
        } catch (Exception e) {
            throw new InternalServerErrorException("SubComment 삭제 중 서버 에러 발생", e);
        }
        log.info("COMPLETE | SubComment 삭제 At " + LocalDateTime.now() + " | " + subCommentId);
    }
}
