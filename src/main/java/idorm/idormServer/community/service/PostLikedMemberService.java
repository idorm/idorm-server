package idorm.idormServer.community.service;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.domain.PostLikedMember;
import idorm.idormServer.community.repository.PostLikedMemberRepository;
import idorm.idormServer.exceptions.CustomException;
import idorm.idormServer.exceptions.http.InternalServerErrorException;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static idorm.idormServer.exceptions.ErrorCode.*;

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
            throw new CustomException(CANNOT_LIKED_SELF);
        }

        Optional<PostLikedMember> foundOne =
                postLikedMemberRepository.findOneByMemberIdAndPostId(post.getId(), member.getId());

        if (foundOne.isPresent()) {
            throw new CustomException(DUPLICATE_LIKED);
        }

        try {
            PostLikedMember postLikedMember = PostLikedMember.builder()
                    .member(member)
                    .post(post)
                    .build();

            postLikedMemberRepository.save(postLikedMember);

            log.info("COMPLETE | PostLikedMember 저장 At " + LocalDateTime.now() + " | post " + post.getId());
            return postLikedMember.getId();
        } catch (InternalServerErrorException e) {
            throw new InternalServerErrorException("PostLikedMember 저장 중 서버 에러 발생", e);
        }
    }

    @Transactional
    public void deleteById(Long postLikedMemberId) {
        log.info("IN PROGRESS | PostLikedMember 삭제 At " + LocalDateTime.now() + " | post 식별자: " + postLikedMemberId);

        postLikedMemberRepository.findById(postLikedMemberId)
                .orElseThrow(() -> new CustomException(POST_LIKED_MEMBER_NOT_FOUND));

        try {
            postLikedMemberRepository.deleteById(postLikedMemberId);
            log.info("COMPLETE | PostLikedMember 삭제 At " + LocalDateTime.now() + " | post 식별자: " + postLikedMemberId);
        } catch (InternalServerErrorException e) {
            throw new InternalServerErrorException("PostLikedMember 삭제 중 서버 에러 발생", e);
        }
    }

    /**
     * 멤버 식별자와 게시글 식별자로 PostLikedMember 단건 조회
     */
    public PostLikedMember findOneByMemberIdAndPostId(Long memberId, Long postId) {
        log.info("IN PROGRESS | PostLikedMember 단건 조회 At " + LocalDateTime.now() + " | 멤버 식별자 :  " + memberId +
                " | 게시글 식별자 : " + postId);

        PostLikedMember foundPostLikedMember =
                postLikedMemberRepository.findOneByMemberIdAndPostId(postId, memberId)
                .orElseThrow(() -> new CustomException(POST_LIKED_MEMBER_NOT_FOUND));

        log.info("COMPLETE | PostLikedMember 단건 조회 At " + LocalDateTime.now() + " | 멤버 식별자 :  " + memberId +
                " | 게시글 식별자 : " + postId);
        return foundPostLikedMember;
    }

    /**
     * 멤버 식별자로 멤버가 좋아요한 Post 식별자 리스트 조회
     */
    public List<Long> findLikedPostIdsByMemberId(Long memberId) {
        log.info("IN PROGRESS | PostLikedMember 멤버 식별자로 좋아요한 게시글 조회 At " + LocalDateTime.now() + " | 멤버 식별자 " +
                memberId);
        try {
            List<Long> foundLikedPosts = postLikedMemberRepository.findLikedPostsByMemberId(memberId);
            log.info("IN PROGRESS | PostLikedMember 멤버 식별자로 좋아요한 게시글 조회 At " + LocalDateTime.now() +
                    " | 게시글 개수 " + foundLikedPosts.size());
            return foundLikedPosts;
        } catch (InternalServerErrorException e) {
            throw new InternalServerErrorException("PostLikedMemberService 멤버 식별자로 좋아요한 게시글 조회 중 서버 에러 발생", e);
        }
    }

    /**
     * 게시글 식별자로 게시글을 공감한 Member 명 수 카운트 조회
     */
    public int countLikedMemberByPostId(Long postId) {
        log.info("IN PROGRESS | PostLikedMember 게시글 식별자로 좋아요한 멤버 카운트 조회 At " + LocalDateTime.now() +
                " | 게시글 식별자 " + postId);

        try {
            int likedCounts = postLikedMemberRepository.countByPostId(postId);
            log.info("COMPLETE | PostLikedMember 게시글 식별자로 좋아요한 멤버 카운트 조회 At " + LocalDateTime.now() +
                    " | 게시글 식별자 " + postId);
            return likedCounts;
        } catch (InternalServerErrorException e) {
            throw new InternalServerErrorException("PostLikedMemberService 게시글 식별자로 좋아요한 멤버 카운트 조회 중 서버 에러 발생", e);
        }
    }

}
