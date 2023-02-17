package idorm.idormServer.community.service;

import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.repository.CommentRepository;
import idorm.idormServer.exception.CustomException;

import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * DB에 댓글 저장 및 매핑된 Member의 comments에 댓글 추가 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public Comment save(Member member, Comment comment) {
        try {
            Comment savedComment = commentRepository.save(comment);
            member.addComment(comment);
            return savedComment;
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 게시글에 특정 댓글의 존재 여부 확인 |
     * 404(COMMENT_NOT_FOUND)
     */
    public void isExistCommentFromPost(Long postId, Long commentId) {
        if(!commentRepository.existsByIdAndPostId(commentId, postId)) {
            throw new CustomException(COMMENT_NOT_FOUND);
        }
    }

    /**
     * 대댓글의 부모 댓글 식별자 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void saveParentCommentId(Long parentCommentId, Comment subComment) {

        try {
            subComment.setParentCommentId(parentCommentId);
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Comment 단건 조회 |
     * 삭제된 부모 댓글도 조회해야 하므로 DELETED_COMMENT(404)에 대한 예외처리는 하지 않는다. |
     * 404(COMMENT_NOT_FOUND)
     */
    public Comment findById(Long commentId) {

        Comment foundComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
        return foundComment;
    }

    /**
     * Comment 부모 댓글 식별자를 받아서 해당 대댓글 리스트 반환 |
     * 500(SERVER_ERROR)
     */
    public List<Comment> findSubCommentsByParentCommentId(Long postId, Long parentCommentId) {

        try {
            List<Comment> foundSubComments =
                    commentRepository.findAllByPostIdAndParentCommentIdOrderByCreatedAt(postId, parentCommentId);

            return foundSubComments;
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 회원이 작성한 모든 댓글 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Comment> findCommentsByMember(Member member) {

        try {
            return commentRepository.findAllByMemberIdAndIsDeletedFalseOrderByCreatedAtDesc(member.getId());
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 게시글 식별자와 댓글 식별자로 댓글 단건 조회 |
     * 404(COMMENT_NOT_FOUND)
     * 404(DELETED_COMMENT)
     */
    public Comment findByCommentIdAndPost(Post post, Long commentId) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, post.getId())
                .orElseThrow(() -> {
                    throw new CustomException(COMMENT_NOT_FOUND);
                });

        if (comment.getIsDeleted())
            throw new CustomException(DELETED_COMMENT);

        return comment;
    }

    /**
     * 특정 게시글의 댓글 다건 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Comment> findCommentsByPostId(Long postId) {
        try {
            List<Comment> foundComments =
                    commentRepository.findAllByPostIdOrderByCreatedAtAsc(postId);
            return foundComments;
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 게시글 삭제 시 모든 댓글 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteAllCommentByDeletedPost(Post post) {
        List<Comment> foundComments = findCommentsByPostId(post.getId());
        if (foundComments.isEmpty()) {
            return;
        }
        try {
            for (Comment comment : foundComments) {
                comment.delete();
            }
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 댓글 단건 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteComment(Comment comment) {

        try {
            comment.delete();
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 멤버 삭제 시 Comment 도메인의 member_id(FK) 를 null로 변경 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updateMemberNullFromComment(Member member) {

        try {
            List<Comment> foundComments = commentRepository.findAllByMemberId(member.getId());
            for(Comment comment : foundComments) {
                comment.updateMemberNull();
            }
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 댓글 삭제 권한 검증 |
     * 401(UNAUTHORIZED_COMMENT)
     */
    public void validateCommentAuthorization(Comment comment, Member member) {
        if (comment.getMember() == null || !member.getId().equals(comment.getMember().getId())) {
            throw new CustomException(UNAUTHORIZED_COMMENT);
        }
    }
}
