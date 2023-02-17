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
     * DB에 PostLikedMember 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void save(PostLikedMember postLikedMember) {
        try {
            postLikedMemberRepository.save(postLikedMember);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 게시글 공감 저장 |
     * 409(CANNOT_LIKED_SELF)
     * 409(DUPLICATE_LIKED)
     * 500(SERVER_ERROR)
     */
    @Transactional
    public PostLikedMember create(Member member, Post post) {

        if(post.getMember() != null && (post.getMember().getId() == member.getId())) {
            throw new CustomException(CANNOT_LIKED_SELF);
        }

        if (isMemberLikedPost(member, post)) {
            throw new CustomException(DUPLICATE_LIKED);
        }

        PostLikedMember postLikedMember = PostLikedMember.builder()
                .member(member)
                .post(post)
                .build();
        save(postLikedMember);

        try {
            post.addPostLikedMember(postLikedMember);
            member.addPostLikedMemer(postLikedMember);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }

        return postLikedMember;
    }

    /**
     * 게시글 공감 여부 확인 |
     */
    public boolean isMemberLikedPost(Member member, Post post) {
        try {
            return postLikedMemberRepository.existsByMemberIdAndPostId(member.getId(), post.getId());
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 게시글 공감 삭제 |
     * 404(POSTLIKEDMEMBER_NOT_FOUND)
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void delete(Member member, Post post) {

        PostLikedMember postLikedMember = postLikedMemberRepository.findByMemberIdAndPostId(member.getId(), post.getId())
                .orElseThrow(() -> {
                    throw new CustomException(POSTLIKEDMEMBER_NOT_FOUND);
                });

        try {
            post.decrementPostLikedCnt();
            post.removePostLikedMember(postLikedMember);
            member.removePostLikedMember(postLikedMember);
            postLikedMemberRepository.deleteByMemberIdAndPostId(member.getId(), post.getId());
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 회원이 공감한 모든 게시글 식별자 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Long> findAllLikedPostIdByMemberId(Long memberId) {
        try {
            return postLikedMemberRepository.findAllLikedPostByMemberId(memberId);
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 게시글 삭제 시 모든 공감 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteAllLikeByDeletedPost(Post post) {
        try {
            postLikedMemberRepository.deleteAllByPostId(post.getId());
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * 멤버 탈퇴 시 PostLikedMember에 매핑된 Member를 null로 변경 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void removeMember(Member member) {

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
