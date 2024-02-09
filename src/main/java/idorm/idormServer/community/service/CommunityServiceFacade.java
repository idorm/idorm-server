package idorm.idormServer.community.service;

import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.domain.PostLike;
import idorm.idormServer.community.dto.CommentRequest;
import idorm.idormServer.community.dto.PostSaveRequest;
import idorm.idormServer.community.dto.PostUpdateRequest;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.community.domain.PostPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityServiceFacade {

    private final PostService postService;
    private final PostLikedMemberService postLikedMemberService;
    private final CommentService commentService;
    private final PostPhotoService postPhotoService;

    public Post savePost(Member member, PostSaveRequest request) {
        Post post = postService.save(request.toEntity(member));

        if (request.getFiles().size() != 0)
            postPhotoService.savePostPhotos(post, request.getFiles());

        return post;
    }

    public void updatePost(Post post,
                           PostUpdateRequest request,
                           List<PostPhoto> deletePostPhotos) {
        postService.updatePost(post,
                request.getTitle(),
                request.getContent(),
                request.getIsAnonymous(),
                deletePostPhotos);

        if (request.getFiles().size() != 0)
            postPhotoService.savePostPhotos(post, request.getFiles());
    }

    public void deletePostLikes(Post post, PostLike postLikedMember) {
        postLikedMemberService.decrementLikedCountsOfPost(post);
        postLikedMemberService.delete(postLikedMember);
    }

    public void deletePost(Post post,
                           List<PostLike> postLikedMembersFromPost,
                           List<PostPhoto> postPhotosFromPost) {

        if (postLikedMembersFromPost != null) {
            for (PostLike postLikedMember : postLikedMembersFromPost) {
                postLikedMemberService.delete(postLikedMember);
            }
        }

        if (postPhotosFromPost != null) {
            for (PostPhoto postPhoto : postPhotosFromPost) {
                postPhotoService.delete(postPhoto);
            }
        }

        postService.delete(post);
    }

    public Comment saveComment(Member member, Post post, CommentRequest request) {
        Comment comment = commentService.save(request.toEntity(member, post));
        if (request.getParentCommentId() != null)
            commentService.saveParentCommentId(request.getParentCommentId(), comment);
        return comment;
    }
}
