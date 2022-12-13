package idorm.idormServer.community.service;

import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.repository.CommentRepository;
import idorm.idormServer.exceptions.CustomException;

import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static idorm.idormServer.exceptions.ErrorCode.*;
import static idorm.idormServer.exceptions.ErrorCode.ILLEGAL_ARGUMENT_SAME_PK;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * Comment 저장
     */
    @Transactional
    public Comment saveComment(String content,
                               Boolean isAnonymous,
                               Post post,
                               Member member) {

        log.info("IN PROGRESS | Comment 저장 At " + LocalDateTime.now() + " | Post 식별자: " + post.getId());

        try {
            Comment createdComment = Comment.builder()
                    .content(content)
                    .isAnonymous(isAnonymous)
                    .post(post)
                    .member(member)
                    .build();

            Comment savedComment = commentRepository.save(createdComment);
            log.info("COMPLETE | Comment 저장 At " + LocalDateTime.now() + " | Comment 식별자: " + savedComment.getId());
            return savedComment;
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] CommentService saveComment {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 대댓글이라면 부모 댓글 식별자 저장하기
     */
    @Transactional
    public void saveParentCommentId(Long parentCommentId, Long subCommentId) {
        log.info("IN PROGRESS | Comment 부모 댓글 식별자 저장 At " + LocalDateTime.now() +
                " | Parent Comment 식별자: " + parentCommentId + " | Sub Comment 식별자 : " + subCommentId);

        findById(parentCommentId);
        Comment foundSubComment = findById(subCommentId);

        if (foundSubComment.getId() == parentCommentId) {
            throw new CustomException(ILLEGAL_ARGUMENT_SAME_PK);
        }

        foundSubComment.updateParentCommentId(parentCommentId);
        commentRepository.save(foundSubComment);

        log.info("COMPLETE | Comment 부모 댓글 식별자 저장 At " + LocalDateTime.now() + " | Parent Comment 식별자: "
                + parentCommentId);
    }

    /**
     * Comment 단건 조회 (해당 댓글의 전체 대댓글까지 조회되게)
     */
    public Comment findById(Long commentId) {
        log.info("IN PROGRESS | Comment 단건 조회 At " + LocalDateTime.now() + " | " + commentId);

        Comment foundComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));

        log.info("COMPLETE | Comment 단건 조회 At " + LocalDateTime.now() + " | " + commentId);
        return foundComment;
    }

    /**
     * Comment 부모 댓글 식별자를 받아서 해당 대댓글 리스트 반환
     */
    public List<Comment> findSubCommentsByParentCommentId(Long postId, Long parentCommentId) {
        log.info("IN PROGRESS | Comment 부모 식별자를 통한 대댓글들 조회 At " + LocalDateTime.now() + " | 부모 댓글 식별자 " +
                parentCommentId);

        try {
            List<Comment> foundSubComments =
                    commentRepository.findSubCommentsByParentCommentId(parentCommentId, postId);
            log.info("COMPLETE | Comment 부모 식별자를 통한 대댓글들 조회 At " + LocalDateTime.now() + " | 부모 댓글 식별자 " +
                    parentCommentId);
            return foundSubComments;
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] CommentService findSubCommentsByParentCommentId {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Comment 로그인한 멤버가 작성한 모든 댓글들 조회
     */
    public List<Comment> findCommentsByMember(Member member) {
        log.info("IN PROGRESS | Comment 로그인한 멤버가 작성한 댓글 조회 At " + LocalDateTime.now() + " | 멤버 식별자: " +
                member.getId());

        try {
            List<Comment> commentsByMemberId = commentRepository.findCommentsByMemberId(member.getId());
            log.info("COMPLETE | Comment 로그인한 멤버가 작성한 댓글 조회 At " + LocalDateTime.now() + " | 멤버 식별자: " +
                    member.getId());
            return commentsByMemberId;
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] CommentService findCommentsByMember {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Comment 게시글 내 모든 댓글 조회
     */
    public List<Comment> findCommentsByPostId(Long postId) {
        log.info("IN PROGRESS | Comment 게시글 내 모든 댓글 조회 At " + LocalDateTime.now() + " | 게시글 식별자: " + postId);
        List<Comment> foundComments = commentRepository.findCommentsByPostId(postId);
        log.info("COMPLETE | Comment 게시글 내 모든 댓글 조회 At " + LocalDateTime.now() + " | 게시글 식별자: " + postId);
        return foundComments;
    }

    /**
     * Comment 게시글 내 모든 댓글, 대댓글 삭제
     */
    public void deleteCommentsByPostId(Long postId) {
        log.info("IN PROGRESS | Comment 게시글 내 모든 댓글 삭제 At " + LocalDateTime.now() + " | 게시글 식별자: " + postId);
        List<Comment> foundComments = findCommentsByPostId(postId);

        try {
            for (Comment comment : foundComments) {
                comment.deleteComment();
                commentRepository.save(comment);
            }
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] CommentService deleteCommentsByPostId {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
        log.info("COMPLETE | Comment 게시글 내 모든 댓글 삭제 At " + LocalDateTime.now() + " | 게시글 식별자: " + postId);
    }

    /**
     * Comment 댓글 단건 삭제 |
     * 대댓글은 살아있어야 함
     */
    @Transactional
    public void deleteComment(Long commentId, Member member) {
        log.info("IN PROGRESS | Comment 삭제 At " + LocalDateTime.now() + " | " + commentId);
        Comment foundComment = findById(commentId);

        if (foundComment.getMember().getId() != member.getId()) {
            throw new CustomException(UNAUTHORIZED_DELETE);
        }

        try {
            foundComment.deleteComment();
            commentRepository.save(foundComment);
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] CommentService deleteComment {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
        log.info("COMPLETE | Comment 삭제 At " + LocalDateTime.now() + " | " + commentId);
    }
}
