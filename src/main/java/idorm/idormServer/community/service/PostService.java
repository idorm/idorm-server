package idorm.idormServer.community.service;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.repository.PostRepository;
import idorm.idormServer.exceptions.http.InternalServerErrorException;
import idorm.idormServer.exceptions.http.NotFoundException;
import idorm.idormServer.exceptions.http.UnauthorizedException;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import idorm.idormServer.photo.domain.Photo;
import idorm.idormServer.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PhotoService photoService;
    private final MemberService memberService;

    /**
     * Post 게시글 사진 추가 메소드 |
     * 사진 최대 개수 10개임. 넘으면 exception 던져야댐
     * 사진 파일이름 라벨링 작업 필요함
     */
    private List<Photo> savePhotos(Post post, Member member, List<MultipartFile> files) {
        log.info("IN PROGRESS | Post 게시글 사진 저장 At " + LocalDateTime.now() + " | 게시글 식별자: " + post.getId());

        int index = fileNumbering(post.getPhotos());
        List<String> fileNames = new ArrayList<>();

        for(MultipartFile file : files) {
            String fileName = post.getId() + "_" + index + file.getContentType().replace("image/", ".");;
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
    private int fileNumbering(List<Photo> photos) {
        log.info("IN PROGRESS | 파일 번호 부여 At " + LocalDateTime.now() + " | " + photos.size());
        int index = 1;

        if(photos.isEmpty()) {
            return index;
        } else {
            for(Photo photo : photos) {
                String[] fileName = photo.getFileName().split("[.]");
                int fileNameIndex = parseInt(fileName[0]);
                if(fileNameIndex > index) {
                    index = fileNameIndex;
                }
            }
            index += 1;
        }
        log.info("COMPLETE | 파일 번호 부여 At " + LocalDateTime.now() + " | " + index);
        return index;
    }

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

            Post savedPost = postRepository.save(createdPost);
            log.info(savedPost.getId().toString());
            List<Photo> savedPhotos = savePhotos(savedPost, member, files);
            savedPost.addPhotos(savedPhotos);
            log.info("COMPLETE | Post 저장 At " + LocalDateTime.now() + " | Post 식별자: " + createdPost.getId() + " | " + title);
            return createdPost;
        } catch (Exception e) {
            throw new InternalServerErrorException("Post 저장 중 서버 에러 발생", e);
        }
    }

    /**
     * Post 수정 |
     * 게시글 사진을 제외한 부분들을 수정합니다.
     */
    @Transactional
    public void updatePost(
                     Long postId,
                     Member member,
                     String title,
                     String content,
                     Boolean isAnonymous
    ) {
        log.info("IN PROGRESS | Post 수정 At " + LocalDateTime.now() + " | " + title);

        Optional<Post> foundPost = postRepository.findById(postId);

        if (foundPost.isEmpty()) {
            throw new NotFoundException("수정할 게시글이 존재하지 않습니다.");
        }

        if(foundPost.get().getMember().getId() != member.getId()) {
            throw new UnauthorizedException("본인이 작성한 게시글만 수정할 수 있습니다.");
        }

        try {
            foundPost.get().updatePost(title, content, isAnonymous);

            Post updatedPost = postRepository.save(foundPost.get());

            log.info("COMPLETE | Post 수정 At " + LocalDateTime.now() + " | Post 식별자: " + updatedPost.getId());
        } catch (Exception e) {
            throw new InternalServerErrorException("Post 수정 중 서버 에러 발생", e);
        }
    }

    /**
     * Post 게시글 수정 - 사진 추가 |
     * Post 게시글 사진을 추가할 때 사용합니다.
     */
    @Transactional
    public void updatePostAddPhoto(
            Long postId,
            Member member,
            List<MultipartFile> files
    ) {
        log.info("IN PROGRESS | Post 사진 추가 수정 At " + LocalDateTime.now() + " | 게시글 식별자: " + postId);

        Optional<Post> foundPost = postRepository.findById(postId);

        if (foundPost.isEmpty()) {
            throw new NotFoundException("수정할 게시글이 존재하지 않습니다.");
        }

        if(foundPost.get().getMember().getId() != member.getId()) {
            throw new UnauthorizedException("본인이 작성한 게시글만 수정할 수 있습니다.");
        }

        try {

            List<Photo> savedPhotos = savePhotos(foundPost.get(), member, files);
            foundPost.get().addPhotos(savedPhotos);

            postRepository.save(foundPost.get());

            log.info("COMPLETE | Post 사진 추가 수정 At " + LocalDateTime.now() + " | 저장된 사진 개수 " + foundPost.get().getPhotos().size());
        } catch (Exception e) {
            throw new InternalServerErrorException("Post 사진 추가 수정 중 서버 에러 발생", e);
        }
    }


    /**
     * Post 게시글 수정 - 사진 삭제 |
     * Post 게시글 사진을 삭제할 때 사용합니다.
     */
    @Transactional
    public void updatePostDeletePhoto(
            Long postId,
            Member member,
            List<String> fileNames
    ) {
        log.info("IN PROGRESS | Post 게시글 사진 삭제 At " + LocalDateTime.now() + " | 게시글 식별자: " + postId + " | 파일 크기: " + fileNames.size());

        Optional<Post> foundPost = postRepository.findById(postId);

        if (foundPost.isEmpty()) {
            throw new NotFoundException("수정할 게시글이 존재하지 않습니다.");
        }

        if(foundPost.get().getMember().getId() != member.getId()) {
            throw new UnauthorizedException("본인이 작성한 게시글만 수정할 수 있습니다.");
        }

        try {
            // TODO: Post 의 photos 부분에서도 사진 삭제를 해야하는지 확인

            photoService.deletePostPhotos(foundPost.get(), member, fileNames);
            postRepository.save(foundPost.get());

            log.info("COMPLETE | Post 게시글 사진 삭제 At " + LocalDateTime.now() + " | 저장된 사진 개수 " + foundPost.get().getPhotos().size());
        } catch (Exception e) {
            throw new InternalServerErrorException("Post 게시글 사진 삭제 중 서버 에러 발생", e);
        }
    }

    /**
     * Post 삭제 |
     */
    @Transactional
    public void deletePost(Long postId, Member member) {
        Optional<Post> foundPost = postRepository.findById(postId);

        if(foundPost.isEmpty()) {
            throw new NotFoundException("삭제할 게시글이 존재하지 않습니다.");
        }
        if(foundPost.get().getMember().getId() != member.getId()) {
            throw new UnauthorizedException("본인이 작성한 게시글만 삭제할 수 있습니다.");
        }

        foundPost.get().updateIsVisible();
        postRepository.save(foundPost.get());

        // TODO: Post에 연관된 댓글, 대댓글도 isVisible 처리 필요
        // TODO: Member에 매핑된 Post도 삭제 처리 필요
    }

    /**
     * Post 단건 조회 |
     * 게시글 식별자를 통해 조회합니다.
     */
    public Post findById(Long postId) {
        Optional<Post> foundPost = postRepository.findById(postId);

        // TODO: 댓글, 대댓글까지 조회

        if(foundPost.isEmpty()) {
            throw new NotFoundException("조회할 게시글이 존재하지 않습니다.");
        }

        return foundPost.get();
    }

    /**
     * Post 기숙사 카테고리 별 다건 조회 |
     * 기숙사 카테고리를 사용한 쿼리를 통해 해당되는 기숙사의 게시글들을 조회합니다.
     * 페이징이 필요할 듯
     */
    public List<Post> findManyPostsByDormCategory(String dormNum) {
        List<Long> foundPostsId = postRepository.findManyByDormCategory(dormNum);

        List<Post> foundPosts = new ArrayList<>();
        for(Long postId : foundPostsId) {
            foundPosts.add(postRepository.findById(postId).get());
        }
        return foundPosts;
    }

    /**
     * 기숙사 카테고리 별 인기 Post 조회 |
     * 실시간으로 조회 가능하게 할지
     * 일주일 이내의 글만 인기글로 선정
     * 공감 순으로 상위 10개 조회 (만약 동일 공감이 많다면 더 빠른 최신 날짜로)
     */
//    public List<Post> findTopPosts(String dormNum) {
//
//    }

    /**
     * 로그인한 멤버가 작성한 모든 게시글 리스트 조회 |
     */
//    public List<Post> findPostsByMember(Member member) {
//
//    }

}
