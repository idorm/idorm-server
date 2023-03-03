package idorm.idormServer.community.service;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.repository.PostRepository;
import idorm.idormServer.exception.CustomException;

import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.photo.domain.Photo;
import idorm.idormServer.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PhotoService photoService;
    private final PostLikedMemberService postLikedMemberService;
    private final CommentService commentService;

    /**
     * 멤버 탈퇴 시 Post, Comment에 매핑된 Member를 null로 변경 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void removeMember(Member member) {

        try {
            List<Post> foundPosts = postRepository.findAllByMemberId(member.getId());
            for(Post post : foundPosts) {
                post.removeMember();
            }
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
        commentService.updateMemberNullFromComment(member);
    }

    /**
     * DB에 게시글 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public Post save(Member member, Post post) {
        try {
            Post savedPost = postRepository.save(post);
            member.addPost(savedPost);
            return savedPost;
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Post 게시글 수정 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updatePost(
            Post updatePost,
            String title,
            String content,
            Boolean isAnonymous,
            List<Photo> deletePostPhotos
    ) {
        try {
            for (Photo deletePostPhoto : deletePostPhotos) {
                deletePostPhoto.delete();
            }

            updatePost.updatePost(title, content, isAnonymous);
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 게시글 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deletePost(Post post) {

        commentService.deleteAllCommentByDeletedPost(post);
        postLikedMemberService.deleteAllLikeByDeletedPost(post);
        photoService.deleteAllPostPhotoByDeletedPost(post);
        try {
            post.delete();
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 게시글 단건 조회 |
     * 404(POST_NOT_FOUND)
     * 404(DELETED_POST)
     */
    public Post findById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    throw new CustomException(POST_NOT_FOUND);
                });
        if (post.getIsDeleted())
            throw new CustomException(DELETED_POST);
        return post;
    }

    /**
     * 기숙사 카테고리 별 모든 게시글 다건 조회 |
     * 500(SERVER_ERROR)
     */
    public Page<Post> findManyPostsByDormCategory(DormCategory dormCategory, int pageNum) {
        try {
            Page<Post> foundPosts =
                    postRepository.findAllByDormCategoryAndIsDeletedFalseOrderByCreatedAtDesc(
                            dormCategory.getType(),
                            PageRequest.of(pageNum, 10));
            return foundPosts;
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 기숙사 카테고리 별 인기 Post 조회 |
     * 실시간으로 조회 가능하게 할지
     * 일주일 이내의 글만 인기글로 선정
     * 공감 순으로 상위 10개 조회 (만약 동일 공감이 많다면 더 빠른 최신 날짜로)
     */
    public List<Post> findTopPosts(DormCategory dormCategory) {
        try {
            List<Post> foundPosts = postRepository.findTopPostsByDormCategory(dormCategory.getType());

            return foundPosts;
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * FCM 용 기숙사 카테고리 별 인기 게시글 1개 조회 |
     */
    public Post findTopPost(DormCategory dormCategory) {
        try {
            return postRepository.findTopPostByDormCategory(dormCategory.getType());
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 회원이 작성한 모든 게시글 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Post> findPostsByMember(Member member) {
        try {
            return postRepository.findAllByMemberIdAndIsDeletedFalseOrderByUpdatedAtDesc(member.getId());
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 특정 게시글의 삭제되지 않은 게시글 사진들 조회 |
     */
    public List<Photo> findPostPhotosIsDeletedFalse(Post post) {
        return post.getPostPhotosIsDeletedFalse();
    }

    /**
     * 게시글 수정 / 삭제 권한 검증 |
     * 401(UNAUTHORIZED_POST)
     */
    public void validatePostAuthorization(Post post, Member member) {
        if (post.getMember() == null || !member.getId().equals(post.getMember().getId())) {
            throw new CustomException(UNAUTHORIZED_POST);
        }
    }

    /**
     * 게시글 첨부 파일 개수 검증 |
     * 413(FILE_COUNT_EXCEED)
     */
    public void validatePostPhotoCountExceeded(int count) {
        if(count > 10) {
            throw new CustomException(FILE_COUNT_EXCEED);
        }
    }

    /**
     * 게시글 저장 및 수정 시 multipart/form-data 타입의 request dto 검증 |
     * 400(FIELD_REQUIRED)
     * 400(TITLE_LENGTH_INVALID)
     * 400(CONTENT_LENGTH_INVALID)
     */
    public void validatePostRequest(String title, String content, Boolean isAnonymous) {

        if (title == null || title.isEmpty())
            throw new CustomException(FIELD_REQUIRED);
        if (content == null || content.isEmpty())
            throw new CustomException(FIELD_REQUIRED);
        if (isAnonymous == null)
            throw new CustomException(FIELD_REQUIRED);

        if (!(title.length() >= 1 && title.length() <= 30))
            throw new CustomException(TITLE_LENGTH_INVALID);
        if (!(content.length() >= 1 && content.length() <= 300))
            throw new CustomException(CONTENT_LENGTH_INVALID);
    }

}
