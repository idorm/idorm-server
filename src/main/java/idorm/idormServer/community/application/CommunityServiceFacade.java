package idorm.idormServer.community.application;//package idorm.idormServer.community.service;
//
//import idorm.idormServer.community.domain.CommentJpaEntity;
//import idorm.idormServer.community.domain.PostJpaEntity;
//import idorm.idormServer.community.domain.PostLikeJpaEntity;
//import idorm.idormServer.community.dto.CommentRequest;
//import idorm.idormServer.community.dto.PostSaveRequest;
//import idorm.idormServer.community.dto.PostUpdateRequest;
//import idorm.idormServer.member.domain.Member;
//import idorm.idormServer.community.domain.PostPhotoJpaEntity;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class CommunityServiceFacade {
//
//    private final PostService postService;
//    private final PostLikedMemberService postLikedMemberService;
//    private final CommentService commentService;
//    private final PostPhotoService postPhotoService;
//
//    public PostJpaEntity savePost(Member member, PostSaveRequest request) {
//        PostJpaEntity post = postService.save(request.toEntity(member));
//
//        if (request.getFiles().size() != 0)
//            postPhotoService.savePostPhotos(post, request.getFiles());
//
//        return post;
//    }
//
//    public void updatePost(PostJpaEntity post,
//                           PostUpdateRequest request,
//                           List<PostPhotoJpaEntity> deletePostPhotos) {
//        postService.updatePost(post,
//                request.getTitleEmbeddedEntity(),
//                request.getContent(),
//                request.getIsAnonymous(),
//                deletePostPhotos);
//
//        if (request.getFiles().size() != 0)
//            postPhotoService.savePostPhotos(post, request.getFiles());
//    }
//
//    public void deletePostLikes(PostJpaEntity post, PostLikeJpaEntity postLikedMember) {
//        postLikedMemberService.decrementLikedCountsOfPost(post);
//        postLikedMemberService.delete(postLikedMember);
//    }
//
//    public void deletePost(PostJpaEntity post,
//                           List<PostLikeJpaEntity> postLikedMembersFromPost,
//                           List<PostPhotoJpaEntity> postPhotosFromPost) {
//
//        if (postLikedMembersFromPost != null) {
//            for (PostLikeJpaEntity postLikedMember : postLikedMembersFromPost) {
//                postLikedMemberService.delete(postLikedMember);
//            }
//        }
//
//        if (postPhotosFromPost != null) {
//            for (PostPhotoJpaEntity postPhoto : postPhotosFromPost) {
//                postPhotoService.delete(postPhoto);
//            }
//        }
//
//        postService.delete(post);
//    }
//
//    public CommentJpaEntity saveComment(Member member, PostJpaEntity post, CommentRequest request) {
//        CommentJpaEntity comment = commentService.save(request.toEntity(member, post));
//        if (request.getParentCommentId() != null)
//            commentService.saveParentCommentId(request.getParentCommentId(), comment);
//        return comment;
//    }
//}
