package idorm.idormServer.community.service;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.repository.PostRepository;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.matching.domain.DormCategory;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.photo.domain.PostPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * DB에 게시글 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public Post save(Post post) {
        try {
            return postRepository.save(post);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 게시글 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void delete(Post post) {
        try {
            post.delete();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
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
            List<PostPhoto> deletePostPhotos
    ) {
        try {
            for (PostPhoto deletePostPhoto : deletePostPhotos) {
                deletePostPhoto.delete();
            }

            updatePost.updatePost(title, content, isAnonymous);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
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
                    throw new CustomException(null, POST_NOT_FOUND);
                });
        if (post.getIsDeleted())
            throw new CustomException(null, DELETED_POST);
        return post;
    }

    /**
     * 예외 처리 없이 게시글 단건 조회 |
     * 삭제되거나 없는 게시글은 null을 반환한다.
     */
    public Optional<Post> findOptionalById(Long postId) {
        try {
            return Optional.of(postRepository.findByIdAndIsDeletedIsFalse(postId));
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 기숙사 카테고리 별 모든 게시글 다건 조회 |
     * 500(SERVER_ERROR)
     */
    public Page<Post> findManyPostsByDormCategory(DormCategory dormCategory, int pageNum) {
        try {
            Page<Post> foundPosts =
                    postRepository.findAllByDormCategoryAndIsDeletedIsFalseOrderByCreatedAtDesc(
                            dormCategory.getType(),
                            PageRequest.of(pageNum, 10));
            return foundPosts;
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 기숙사 카테고리 별 인기 Post 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Post> findTopPosts(DormCategory dormCategory) {
        try {
            return postRepository.findTopPostsByDormCategory(dormCategory.getType());
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * FCM 용 기숙사 카테고리 별 인기 게시글 1개 조회 |
     */
    public Post findTopPost(DormCategory dormCategory) {
        try {
            return postRepository.findTopPostByDormCategory(dormCategory.getType());
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 회원이 작성한 모든 게시글 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Post> findPostsByMember(Member member) {
        try {
            return postRepository.findAllByMemberIdAndIsDeletedIsFalseOrderByUpdatedAtDesc(member.getId());
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 특정 게시글의 삭제되지 않은 게시글 사진들 조회 |
     */
    public List<PostPhoto> findAllPostPhotosFromPost(Post post) {
        return post.getPostPhotosIsDeletedIsFalse();
    }

    /**
     * 게시글 수정 / 삭제 권한 검증 |
     * 403(ACCESS_DENIED_POST)
     */
    public void validatePostAuthorization(Post post, Member member) {
        if (post.getMember() == null || !member.getId().equals(post.getMember().getId())) {
            throw new CustomException(null, ACCESS_DENIED_POST);
        }
    }

    /**
     * 게시글 첨부 파일 개수 검증 |
     * 413(FILE_COUNT_EXCEED)
     */
    public void validatePostPhotoCountExceeded(int count) {
        if(count > 10) {
            throw new CustomException(null, FILE_COUNT_EXCEED);
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
            throw new CustomException(null, FIELD_REQUIRED);
        if (content == null || content.isEmpty())
            throw new CustomException(null,FIELD_REQUIRED);
        if (isAnonymous == null)
            throw new CustomException(null,FIELD_REQUIRED);

        if (!(title.length() >= 1 && title.length() <= 30))
            throw new CustomException(null,TITLE_LENGTH_INVALID);
        if (!(content.length() >= 1 && content.length() <= 300))
            throw new CustomException(null,CONTENT_LENGTH_INVALID);
    }
}
