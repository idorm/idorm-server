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
     * 409(CANNOT_LIKED_SELF)
     * 409(DUPLICATE_LIKED)
     */
    @Transactional
    public PostLikedMember savePostLikedMember(Member member, Post post) {

        if(post.getMember() != null && (post.getMember().getId() == member.getId())) {
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

            return postLikedMember;
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    public boolean isMemberLikedPost(Member member, Post post) {
        try {
            return postLikedMemberRepository.existsByMemberIdAndPostId(member.getId(), post.getId());
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    @Transactional
    public void deletePostLikedMember(Member member, Post post) {
        boolean likedYn = isMemberLikedPost(member, post);

        if(likedYn == false) {
            throw new CustomException(POSTLIKEDMEMBER_NOT_FOUND);
        }

        try {
            postLikedMemberRepository.deleteByMemberIdAndPostId(member.getId(), post.getId());
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 멤버가 공감한 게시글 식별자 목록 조회
     */
    public List<Long> findLikedPostIdsByMemberId(Long memberId) {
        try {
            List<Long> foundLikedPosts = postLikedMemberRepository.findLikedPostsByMemberId(memberId);
            return foundLikedPosts;
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 게시글 삭제 시 해당 공감 전부 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteAllLikesFromPost(Post post) {
        try {
            postLikedMemberRepository.deleteAllByPostId(post.getId());
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 멤버 탈퇴 시 PostLikedMember에 Member를 null로 수정 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updateMemberNull(Member member) {

        try {
            List<PostLikedMember> postLikedMembers = postLikedMemberRepository.findAllByMemberId(member.getId());
            if (postLikedMembers.isEmpty()) {
                return;
            }
            for (PostLikedMember postLikedMember : postLikedMembers) {
                postLikedMember.removeMember();
            }
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }
}
