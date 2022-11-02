package idorm.idormServer.community.service;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.domain.PostLikedMember;
import idorm.idormServer.community.repository.PostLikedMemberRepository;
import idorm.idormServer.exceptions.http.ConflictException;
import idorm.idormServer.exceptions.http.InternalServerErrorException;
import idorm.idormServer.exceptions.http.NotFoundException;
import idorm.idormServer.member.domain.Member;
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
public class PostLikedMemberService {

    private final PostLikedMemberRepository postLikedMemberRepository;

    @Transactional
    public Long save(Member member, Post post) {
        log.info("IN PROGRESS | PostLikedMember 저장 At " + LocalDateTime.now() + " | post " + post.getId());

        if(post.getMember().getId() == member.getId()) {
            throw new ConflictException("본인이 작성한 게시글에 공감할 수 없습니다.");
        }

        Optional<PostLikedMember> foundOne = postLikedMemberRepository.findOneByMemberIdAndPostId(post.getId(), member.getId());

        if (foundOne.isPresent()) {
            throw new ConflictException("이미 공감한 게시글에는 공감할 수 없습니다.");
        }

        try {
            PostLikedMember postLikedMember = PostLikedMember.builder()
                    .member(member)
                    .post(post)
                    .build();
            postLikedMemberRepository.save(postLikedMember);
            log.info("COMPLETE | PostLikedMember 저장 At " + LocalDateTime.now() + " | post " + post.getId());
            return postLikedMember.getId();
        } catch (Exception e) {
            throw new InternalServerErrorException("PostLikedMember 저장 중 서버 에러 발생", e);
        }
    }

    @Transactional
    public void deleteById(Long postLikedMemberId) {
        log.info("IN PROGRESS | PostLikedMember 삭제 At " + LocalDateTime.now() + " | post 식별자: " + postLikedMemberId);
        findOneById(postLikedMemberId);
        try {
            postLikedMemberRepository.deleteById(postLikedMemberId);
            log.info("COMPLETE | PostLikedMember 삭제 At " + LocalDateTime.now() + " | post 식별자: " + postLikedMemberId);
        } catch (Exception e) {
            throw new InternalServerErrorException("PostLikedMember 삭제 중 서버 에러 발생", e);
        }
    }

    /**
     * 식별자로 PostLikedMember 단건 조회
     */
    public PostLikedMember findOneById(Long postLikedMemberId) {
        log.info("IN PROGRESS | PostLikedMember 단건 조회 At " + LocalDateTime.now() + " | 식별자 :  " + postLikedMemberId);
        Optional<PostLikedMember> foundPostLikedMember = postLikedMemberRepository.findById(postLikedMemberId);

        if(foundPostLikedMember.isEmpty()) {
            throw new NotFoundException("게시글을 멤버가 공감하지 않았습니다.");
        }
        log.info("COMPLETE | PostLikedMember 단건 조회 At " + LocalDateTime.now() + " | 식별자 :  " + postLikedMemberId);
        return foundPostLikedMember.get();
    }

    /**
     * 멤버 식별자와 게시글 식별자로 PostLikedMember 단건 조회
     */
    public PostLikedMember findOneByMemberIdAndPostId(Long memberId, Long postId) {
        log.info("IN PROGRESS | PostLikedMember 단건 조회 At " + LocalDateTime.now() + " | 멤버 식별자 :  " + memberId + " | 게시글 식별자 : " + postId);
        Optional<PostLikedMember> foundPostLikedMember = postLikedMemberRepository.findOneByMemberIdAndPostId(postId, memberId);

        if(foundPostLikedMember.isEmpty()) {
            throw new NotFoundException("게시글을 멤버가 공감하지 않았습니다.");
        }
        log.info("COMPLETE | PostLikedMember 단건 조회 At " + LocalDateTime.now() + " | 멤버 식별자 :  " + memberId + " | 게시글 식별자 : " + postId);
        return foundPostLikedMember.get();
    }

    /**
     * 멤버 식별자로 멤버가 좋아요한 Post 식별자 리스트 조회
     */
    public List<Long> findLikedPostIdsByMemberId(Long memberId) {
        log.info("IN PROGRESS | PostLikedMember 멤버 식별자로 좋아요한 게시글 조회 At " + LocalDateTime.now() + " | 멤버 식별자 " + memberId);
        try {
            List<Long> foundLikedPosts = postLikedMemberRepository.findLikedPostsByMemberId(memberId);
            log.info("IN PROGRESS | PostLikedMember 멤버 식별자로 좋아요한 게시글 조회 At " + LocalDateTime.now() + " | 게시글 개수 " + foundLikedPosts.size());
            return foundLikedPosts;
        } catch (Exception e) {
            throw new InternalServerErrorException("PostLikedMemberService 멤버 식별자로 좋아요한 게시글 조회 중 서버 에러 발생", e);
        }
    }

    /**
     * 게시글 식별자로 게시글을 공감한 Member 식별자 리스트 조회
     */
    public List<Long> findLikedMemberIdsByPostId(Long postId) {
        log.info("IN PROGRESS | PostLikedMember 게시글 식별자로 좋아요한 멤버 조회 At " + LocalDateTime.now() + " | 게시글 식별자 " + postId);
        try {
            List<Long> foundLikedMembers = postLikedMemberRepository.findLikedMemberByPostId(postId);
            log.info("IN PROGRESS | PostLikedMember 게시글 식별자로 좋아요한 멤버 조회 At " + LocalDateTime.now() + " | 좋아요 멤버 명수 " + foundLikedMembers.size());
            return foundLikedMembers;
        } catch (Exception e) {
            throw new InternalServerErrorException("PostLikedMemberService 게시글 식별자로 좋아요한 멤버 조회 중 서버 에러 발생", e);
        }
    }

}
