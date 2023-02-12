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
     * Comment 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public Comment saveComment(String content,
                               Boolean isAnonymous,
                               Post post,
                               Member member) {

        try {
            Comment createdComment = Comment.builder()
                    .content(content)
                    .isAnonymous(isAnonymous)
                    .post(post)
                    .member(member)
                    .build();

            Comment savedComment = commentRepository.save(createdComment);
            return savedComment;
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 대댓글일 때 해당 게시글에 부모 댓글이 저장되어 있는지 확인하기
     */
    public void isExistParentCommentFromPost(Long postId, Long commentId) {
        boolean isExistParentComment = commentRepository.existsByIdAndPostId(commentId, postId);
        if(isExistParentComment == false) {
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
     * Comment 로그인한 멤버가 작성한 모든 댓글들 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Comment> findCommentsByMember(Member member) {

        try {
            List<Comment> commentsByMemberId =
                    commentRepository.findAllByMemberIdAndIsDeletedOrderByUpdatedAtDesc(member.getId(), false);
            return commentsByMemberId;
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 특정 게시글의 댓글 다건 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Comment> findCommentsByPostId(Long postId) {
        try {
            List<Comment> foundComments =
                    commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);
            return foundComments;
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Comment 게시글 내 모든 댓글, 대댓글 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteCommentsByPost(Post post) {
        List<Comment> foundComments = findCommentsByPostId(post.getId());
        if (foundComments.isEmpty()) {
            return;
        }
        try {
            for (Comment comment : foundComments) {
                comment.deleteComment();
            }
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Comment 댓글 단건 삭제 |
     * 401(UNAUTHORIZED_DELETE)
     * 404(DELETED_COMMENT)
     */
    @Transactional
    public void deleteComment(Long commentId, Member member) {
        Comment foundComment = findById(commentId);

        if (foundComment.getMember().getId() != member.getId()) {
            throw new CustomException(UNAUTHORIZED_DELETE);
        }

        if (foundComment.getIsDeleted() == true) {
            throw new CustomException(DELETED_COMMENT);
        }

        try {
            foundComment.deleteComment();
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
}
