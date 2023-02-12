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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
     * 멤버 삭제 시 Post, Comment 도메인의 member_id(FK) 를 null로 변경해주어야 한다.
     */
    @Transactional
    public void updateMemberNull(Member member) {

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
     * Post 게시글 사진 추가 메소드 |
     */
    private List<Photo> savePostPhotos(Post post, List<MultipartFile> files) {

        int index = 1;
        List<String> fileNames = new ArrayList<>();

        for(MultipartFile file : files) {
            String fileName = index + file.getContentType().replace("image/", ".");;
            fileNames.add(fileName);
            index += 1;
        }

        List<Photo> savedPhotos = photoService.savePostPhotos(post, fileNames, files);

        return savedPhotos;
    }

    /**
     * Post 저장 |
     * Photo까지 저장되어야한다.
     */
    @Transactional
    public Post savePost(Member member,
                     List<MultipartFile> files,
                     DormCategory dormCategory,
                     String title,
                     String content,
                     Boolean isAnonymous
    ) {

        try {
            Post createdPost = Post.builder()
                    .member(member)
                    .dormCategory(dormCategory)
                    .title(title)
                    .content(content)
                    .isAnonymous(isAnonymous)
                    .build();

            Post savedPost = postRepository.save(createdPost);
            savePostPhotos(savedPost, files);

            return createdPost;
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }


    /**
     * Post 게시글 수정
     * 작성한 멤버가 맞는지 확인 후 게시글 식별자를 통해 게시글을 수정합니다.
     * 수정할 수 있는 요소는 제목, 내용, 익명여부, 사진 입니다.
     */
    @Transactional
    public void updatePost(
            Long postId,
            String title,
            String content,
            Boolean isAnonymous,
            List<MultipartFile> files
    ) {

        Post foundPost = findById(postId);

        // TODO: 수정된 사진만 수정되게 변경
        photoService.deletePostFullPhotos(foundPost);

        try {
            foundPost.updatePost(title, content, isAnonymous);

            List<Photo> updatedPhotos = savePostPhotos(foundPost, files);
            foundPost.addPostPhotos(updatedPhotos);

            postRepository.save(foundPost);

        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Post 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deletePost(Post post) {

        commentService.deleteCommentsByPost(post);
        postLikedMemberService.deleteAllLikesFromPost(post);
        photoService.deletePhotoDeletingPost(post);
        try {
            post.deletePost();
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Post 단건 조회 |
     * 404(POST_NOT_FOUND)
     * 404(DELETED_POST)
     */
    public Post findById(Long postId) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        if(foundPost.getIsDeleted() == true) {
            throw new CustomException(DELETED_POST);
        }
        return foundPost;
    }

    /**
     * Post 기숙사 카테고리 별 다건 조회 |
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
     * 특정 멤버가 작성한 모든 게시글 리스트 조회 |
     */
    public List<Post> findPostsByMember(Member member) {
        try {
            List<Post> postsByMemberId =
                    postRepository.findAllByMemberIdAndIsDeletedFalseOrderByUpdatedAtDesc(member.getId());

            return postsByMemberId;
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 게시글 수정 / 삭제 권한 검증 |
     * 401(POST_NOT_ALLOWED)
     */
    public void validatePostAuthorization(Post post, Member member) {
        if (!member.getId().equals(post.getMember().getId())) {
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
}
