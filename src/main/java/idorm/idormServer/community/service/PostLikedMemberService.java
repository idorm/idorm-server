package idorm.idormServer.community.service;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.domain.PostLikedMember;
import idorm.idormServer.community.repository.PostLikedMemberRepository;
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
public class PostLikedMemberService {

    private final PostLikedMemberRepository postLikedMemberRepository;

    /**
     * 게시글 공감 저장 |
     * 409(CANNOT_LIKED_POST_BY_DELETED_MEMBER)
     * 409(CANNOT_LIKED_SELF)
     * 409(DUPLICATE_LIKED)
     * 500(SERVER_ERROR)
     */
    @Transactional
    public PostLikedMember create(Member member, Post post) {

        try {
            PostLikedMember postLikedMember = PostLikedMember.builder()
                    .member(member)
                    .post(post)
                    .build();

            return postLikedMemberRepository.save(postLikedMember);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 게시글 공감 삭제 |
     * 404(POSTLIKEDMEMBER_NOT_FOUND)
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void delete(PostLikedMember postLikedMember) {

        try {
            postLikedMember.delete();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 게시글에 공감 수 카운트 다운 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void decrementLikedCountsOfPost(Post post) {
        try {
            post.decrementPostLikedCnt();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 게시글과 회원으로 공감 단건 조회 |
     * 404(POSTLIKEDMEMBER_NOT_FOUND)
     */
    public PostLikedMember findOneByPostAndMember(Post post, Member member) {

        return postLikedMemberRepository.findByMemberIdAndPostIdAndIsDeletedIsFalse(member.getId(), post.getId())
                .orElseThrow(() -> new CustomException(null, POSTLIKEDMEMBER_NOT_FOUND));
    }

    /**
     * 회원이 공감한 모든 게시글 식별자 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Long> findAllLikedPostIdByMemberId(Long memberId) {
        try {
            return postLikedMemberRepository.findAllByMemberId(memberId);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 게시글로 전체 공감 조회 |
     * 500(SERVER_ERROR)
     */
    public List<PostLikedMember> findAllByPost(Post post) {
        try {
            return postLikedMemberRepository.findAllByPostAndIsDeletedIsFalse(post);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 회원 - 게시글 공감 존재 여부 확인 |
     * 500(SERVER_ERROR)
     */
    public boolean isMemberLikedPost(Member member, Post post) {
        try {
            return postLikedMemberRepository.existsByMemberIdAndPostIdAndIsDeletedIsFalse(member.getId(), post.getId());
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }
}
