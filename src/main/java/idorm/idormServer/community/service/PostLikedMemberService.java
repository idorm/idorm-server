package idorm.idormServer.community.service;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.domain.PostLikedMember;
import idorm.idormServer.community.repository.PostLikedMemberRepository;
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

        if(post.getMember() != null && post.getMember().getId() == member.getId()) {
            throw new CustomException(CANNOT_LIKED_SELF);
        }

        boolean postLikedYn = isMemberLikedPost(member, post);
        if (postLikedYn == true) {
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
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] PostLikedMemberService save {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

    public boolean isMemberLikedPost(Member member, Post post) {
        return postLikedMemberRepository.existsByMemberIdAndPostId(member.getId(), post.getId());
    }

    @Transactional
    public void deleteById(Member member, Post post) {
        log.info("IN PROGRESS | PostLikedMember deleteById At " + LocalDateTime.now());

        boolean likedYn = isMemberLikedPost(member, post);

        if(likedYn == false) {
            throw new CustomException(POST_LIKED_MEMBER_NOT_FOUND);
        }

        try {
            postLikedMemberRepository.deleteByMemberIdAndPostId(member.getId(), post.getId());
            log.info("COMPLETE | PostLikedMember deleteById At " + LocalDateTime.now());
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] PostLikedMemberService deleteById {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 멤버가 공감한 게시글 식별자 목록 조회
     */
    public List<Long> findLikedPostIdsByMemberId(Long memberId) {
        log.info("IN PROGRESS | PostLikedMember 멤버 식별자로 좋아요한 게시글 조회 At " + LocalDateTime.now() + " | 멤버 식별자 " +
                memberId);
        try {
            List<Long> foundLikedPosts = postLikedMemberRepository.findLikedPostsByMemberId(memberId);
            log.info("IN PROGRESS | PostLikedMember 멤버 식별자로 좋아요한 게시글 조회 At " + LocalDateTime.now() +
                    " | 게시글 개수 " + foundLikedPosts.size());
            return foundLikedPosts;
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] PostLikedMemberService findLikedPostIdsByMemberId {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 게시글 삭제 시 해당 공감 전부 삭제
     */
    @Transactional
    public void deleteAllLikesFromPost(Post post) {
        try {
            postLikedMemberRepository.deleteAllByPostId(post.getId());
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] PostLikedMemberService deleteAllLikesFromPost {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
    }


}
