package idorm.idormServer.community.service;

import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.domain.PostLikedMember;
import idorm.idormServer.community.repository.PostRepository;
import idorm.idormServer.exceptions.CustomException;

import idorm.idormServer.member.domain.Member;
import idorm.idormServer.photo.domain.Photo;
import idorm.idormServer.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static idorm.idormServer.exceptions.ErrorCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PhotoService photoService;
    private final PostLikedMemberService postLikedMemberService;
    private final CommentService commentService;

    /**
     * 멤버 삭제 시 Post 도메인의 member_id(FK) 를 null로 변경해주어야 한다.
     */
    @Transactional
    public void updateMemberIdFromPost(Member member) {
        List<Post> foundPosts = findPostsByMember(member);

        try {
            for(Post post : foundPosts) {
                post.updateMember();
            }
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] PostService updateMemberIdFromPost {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
        updateMemberIdFromComment(member);
    }

    /**
     * 멤버 삭제 시 Comment 도메인의 member_id(FK) 를 null로 변경해주어야 한다.
     */
    private void updateMemberIdFromComment(Member member) {
        List<Comment> foundComments = commentService.findCommentsByMember(member);

        try {
            for(Comment comment : foundComments) {
                comment.updateMember();
            }
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] PostService updateMemberIdFromComment {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }


    /**
     * Post 게시글 사진 추가 메소드 |
     */
    private List<Photo> savePhotos(Post post, Member member, List<MultipartFile> files) {
        log.info("IN PROGRESS | Post 게시글 사진 저장 At " + LocalDateTime.now() + " | 게시글 식별자: " + post.getId());

        int index = 1;
        List<String> fileNames = new ArrayList<>();

        for(MultipartFile file : files) {
            String fileName = index + file.getContentType().replace("image/", ".");;
            fileNames.add(fileName);
            index += 1;
        }

        List<Photo> savedPhotos = photoService.savePostPhotos(post, member, fileNames, files);

        log.info("COMPLETE | Post 게시글 사진 저장 At " + LocalDateTime.now() + " | 사진 크기: " + savedPhotos.size());
        return savedPhotos;
    }

    /**
     * 파일 번호 부여 |
     * 현재 게시글에 저장되어 있는 사진들의 파일 이름을 확인하여 가장 마지막으로 부여한 인덱스에 1을 더하여 리턴합니다. 이 번호는 새로 추가할 다음 파일 이름을
     * 부여하는데 사용합니다.
     */
//    private int fileNumbering(List<Photo> photos) {
//        log.info("IN PROGRESS | 파일 번호 부여 At " + LocalDateTime.now() + " | " + photos.size());
//        int index = 1;
//
//        if(photos.isEmpty()) {
//            return index;
//        } else {
//            for(Photo photo : photos) {
//                String[] fileName = photo.getFileName().split("[.]");
//                int fileNameIndex = parseInt(fileName[0]);
//                if(fileNameIndex > index) {
//                    index = fileNameIndex;
//                }
//            }
//            index += 1;
//        }
//        log.info("COMPLETE | 파일 번호 부여 At " + LocalDateTime.now() + " | " + index);
//        return index;
//    }

    /**
     * Post 저장 |
     * Photo까지 저장되어야한다.
     */
    @Transactional
    public Post savePost(Member member,
                     List<MultipartFile> files,
                     String dormNum,
                     String title,
                     String content,
                     Boolean isAnonymous
    ) {
        log.info("IN PROGRESS | Post 저장 At " + LocalDateTime.now() + " | " + title);

        try {
            Post createdPost = Post.builder()
                    .member(member)
                    .dormNum(dormNum)
                    .title(title)
                    .content(content)
                    .isAnonymous(isAnonymous)
                    .build();

            createdPost.updateImagesCount(files.size());
            Post savedPost = postRepository.save(createdPost);
            List<Photo> savedPhotos = savePhotos(savedPost, member, files);
            savedPost.addPhotos(savedPhotos);

            log.info("COMPLETE | Post 저장 At " + LocalDateTime.now() + " | Post 식별자: " + createdPost.getId() + " | " +
                    title);

            return createdPost;
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] PostService savePost {} {}", e.getCause(), e.getMessage());
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
            Member member,
            String title,
            String content,
            Boolean isAnonymous,
            List<MultipartFile> files
    ) {
        log.info("IN PROGRESS | Post 수정 At " + LocalDateTime.now() + " | " + title + " | 수정 사진 개수 : " + files.size());

        Post foundPost = findById(postId);
        photoService.deletePostFullPhotos(foundPost, member);

        try {
            foundPost.updatePost(title, content, isAnonymous);

            List<Photo> updatedPhotos = savePhotos(foundPost, member, files);
            foundPost.addPhotos(updatedPhotos);

            foundPost.updateImagesCount(files.size());

            Post updatedPost = postRepository.save(foundPost);

            log.info("COMPLETE | Post 수정 At " + LocalDateTime.now() + " | Post 식별자: " + updatedPost.getId()
                    + " | 저장된 사진 개수 " + foundPost.getImagesCount());
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] PostService updatePost {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * Post 삭제 |
     */
    @Transactional
    public void deletePost(Long postId, Member member) {
        log.info("IN PROGRESS | Post 삭제 At " + LocalDateTime.now() + " | " + postId);
        Post foundPost = findById(postId);

        if(foundPost.getMember() == null || foundPost.getMember().getId() != member.getId()) {
            throw new CustomException(UNAUTHORIZED_DELETE);
        }

        commentService.deleteCommentsByPostId(postId);
        postLikedMemberService.deleteAllLikesFromPost(foundPost);

        try {
            foundPost.deletePost();
            foundPost.deleteLikesCount();
            postRepository.save(foundPost);
            photoService.deletePhotoDeletingPost(postId);
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] PostService deletePost {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }

        log.info("COMPLETE | Post 삭제 At " + LocalDateTime.now() + " | " + postId);
    }

    /**
     * Post 단건 조회 |
     * 게시글 식별자를 통해 조회합니다.
     */
    public Post findById(Long postId) {
        log.info("IN PROGRESS | Post 단건 조회 At " + LocalDateTime.now() + " | " + postId);

        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        if(foundPost.getIsDeleted() == true) {
            throw new CustomException(DELETED_POST);
        }

        log.info("COMPLETE | Post 단건 조회 At " + LocalDateTime.now() + " | " + postId);
        return foundPost;
    }

    /**
     * Post 기숙사 카테고리 별 다건 조회 |
     * 기숙사 카테고리를 사용한 쿼리를 통해 해당되는 기숙사의 게시글들을 조회합니다.
     * TODO: 페이징 처리
     */
    public Page<Post> findManyPostsByDormCategory(String dormNum) {
        log.info("IN PROGRESS | Post 기숙사 카테고리 별 다건 조회 At " + LocalDateTime.now() + " | " + dormNum);

        try {
            Page<Post> foundPosts =
                    postRepository.findAllByDormNumAndIsDeletedOrderByCreatedAtDesc(dormNum, false, PageRequest.of(0, 10));
            log.info("COMPLETE | Post 기숙사 카테고리 별 다건 조회 At " + LocalDateTime.now() + " | " + dormNum);
            return foundPosts;
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] PostService findManyPostsByDormCategory {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 기숙사 카테고리 별 인기 Post 조회 |
     * 실시간으로 조회 가능하게 할지
     * 일주일 이내의 글만 인기글로 선정
     * 공감 순으로 상위 10개 조회 (만약 동일 공감이 많다면 더 빠른 최신 날짜로)
     */
    public List<Post> findTopPosts(String dormNum) {
        log.info("IN PROGRESS | Post 기숙사 카테고리 별 인기 게시글 조회 At " + LocalDateTime.now() + " | " + dormNum);
        try {
            List<Post> foundPosts = postRepository.findTopPostsByDormCategory(dormNum);
            log.info("COMPLETE | Post 기숙사 카테고리 별 인기 게시글 조회 At " + LocalDateTime.now() + " | 조회된 게시글 수: " +
                    foundPosts.size());
            return foundPosts;
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] PostService findTopPosts {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 특정 멤버가 작성한 모든 게시글 리스트 조회 |
     */
    public List<Post> findPostsByMember(Member member) {
        log.info("IN PROGRESS | Post 멤버가 작성한 게시글 조회 At " + LocalDateTime.now() + " | " + member.getId());

        try {
            List<Post> postsByMemberId =
                    postRepository.findAllByMemberIdAndIsDeletedOrderByUpdatedAtDesc(member.getId(), false);

            log.info("COMPLETE | Post 멤버가 작성한 게시글 조회 At " + LocalDateTime.now() + " | 게시글 수 " +
                    postsByMemberId.size());
            return postsByMemberId;
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] PostService findPostsByMember {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 게시글에 멤버가 공감했을 때
     * Post 공감 카운트 증가 및 PostLikedMember save 호출
     */
    @Transactional
    public Long addPostLikes(Member member, Post post) {

        Long savedPostLikedMemberId = postLikedMemberService.save(member, post);
        try {
            post.addLikesCount();
            postRepository.save(post);
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] PostService addPostLikes {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
        return savedPostLikedMemberId;
    }

    /**
     * 게시글에 멤버가 공감 취소했을 때
     * Post 공감 카운트 감소 및 PostLikedMember deletePostLikes 호출
     */
    @Transactional
    public void deletePostLikes(Member member, Post post) {

        if(post.getLikesCount() <= 0) {
            throw new CustomException(LIKED_NOT_FOUND);
        }
        postLikedMemberService.deleteById(member, post);

        try {
            post.subtractLikesCount();
            postRepository.save(post);
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] PostService deletePostLikes {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

}
