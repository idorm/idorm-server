package idorm.idormServer.community.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.dto.post.PostDefaultResponseDto;
import idorm.idormServer.community.dto.post.PostLikedMembersCountByPostResponseDto;
import idorm.idormServer.community.service.PostLikedMemberService;
import idorm.idormServer.community.service.PostService;
import idorm.idormServer.community.vo.post.SavePostVo;
import idorm.idormServer.community.vo.post.UpdatePostVo;
import idorm.idormServer.exceptions.http.NotFoundException;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "Community - Post API")
public class PostController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PostService postService;
    private final PostLikedMemberService postLikedMemberService;

    @PostMapping(value = "/member/post/{postId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Post 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 수정 완료"),
            @ApiResponse(code = 401, message = "수정 권한이 없을 때의 응답입니다."),
            @ApiResponse(code = 404, message = "찾고자 하는 게시글이 없어 요청을 처리하지 못하였을 때의 응답입니다."),
            @ApiResponse(code = 500, message = "Post update 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> updatePost(
            @PathVariable("postId") Long updatePostId,
            @ModelAttribute @Valid UpdatePostVo updateRequest
    ) throws IOException {

        Member member = memberService.findById(updateRequest.getMemberId());
        Post foundPost = postService.findById(updatePostId);

        postService.updatePost(updatePostId,
                member,
                updateRequest.getTitle(),
                updateRequest.getContent(),
                updateRequest.getIsAnonymous(),
                updateRequest.getFiles());

        PostDefaultResponseDto response = new PostDefaultResponseDto(foundPost);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 수정 완료")
                        .data(response)
                        .build()
                );
    }

    @PostMapping(value = "/member/post",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Post 저장")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 저장 완료"),
            @ApiResponse(code = 401, message = "로그인이 필요합니다."),
            @ApiResponse(code = 404, message = "올바른 기숙사 분류가 아닙니다."),
            @ApiResponse(code = 500, message = "Post save 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> savePost(
            @ModelAttribute @Valid SavePostVo postRequest
    ) throws IOException {

        // TODO: 로그인한 멤버 받아올 수 있는 방법 찾아야 함 (security multipart csrf)
        // TODO: 로그인한 멤버 식별자와 입력받은 멤버 식별자가 다른지 확인 필요
        // TODO: files 개수 10개 제한

        if(!postRequest.getDormNum().equals("DORM1") &&
                !postRequest.getDormNum().equals("DORM2") &&
                !postRequest.getDormNum().equals("DORM3")) {
            throw new NotFoundException("올바른 기숙사 분류가 아닙니다.");
        }

        Member member = memberService.findById(postRequest.getMemberId());

        Post createdPost = postService.savePost(
                member,
                postRequest.getFiles(),
                postRequest.getDormNum(),
                postRequest.getTitle(),
                postRequest.getContent(),
                postRequest.getIsAnonymous());

        PostDefaultResponseDto response = new PostDefaultResponseDto(createdPost);

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 저장 완료")
                        .data(response)
                        .build()
                );
    }

    @GetMapping("/member/posts/{dormitory-category}")
    @ApiOperation(value = "Post 기숙사 카테고리로 필터링된 게시글 다건 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 다건 조회 완료"),
            @ApiResponse(code = 401, message = "로그인이 필요합니다."),
            @ApiResponse(code = 404, message = "올바른 기숙사 분류가 아닙니다."),
            @ApiResponse(code = 500, message = "Post 다건 조회 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> findPostsFilteredByCategory(
            @RequestParam(value = "dormitory-category", required = false, defaultValue = "DORM1") String dormNum
    ) {

        if(!dormNum.equals("DORM1") && !dormNum.equals("DORM2") && !dormNum.equals("DORM3")) {
            throw new NotFoundException("올바른 기숙사 분류가 아닙니다.");
        }

        List<Post> posts = postService.findManyPostsByDormCategory(dormNum);

        List<PostDefaultResponseDto> response = posts.stream()
                .map(post -> new PostDefaultResponseDto(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 다건 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @GetMapping("/member/post/{postId}")
    @ApiOperation(value = "Post 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 단건 조회 완료"),
            @ApiResponse(code = 401, message = "로그인이 필요합니다."),
            @ApiResponse(code = 404, message = "조회할 게시글이 존재하지 않습니다"),
            @ApiResponse(code = 500, message = "Post 단건 조회 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> findOnePost(
            @PathVariable("postId") Long postId
    ) {

        // TODO: Post 단건 조회 로직 - 댓글, 대댓글까지 조회
        Post foundPost = postService.findById(postId);

        // TODO: 댓글, 대댓글 리스트 반환
        // TODO: 멤버 정보 반환 (익명 여부 체크 후)
        PostDefaultResponseDto response = new PostDefaultResponseDto(foundPost);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 단건 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @GetMapping("/member/posts/{dormitory-category}/top")
    @ApiOperation(value = "Post 기숙사 카테고리로 필터링된 인기 게시글 다건 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 인기 게시글 조회 완료"),
            @ApiResponse(code = 204, message = "Post 인기 게시글이 없습니다."),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 404, message = "올바른 기숙사 분류가 아닙니다."),
            @ApiResponse(code = 500, message = "Post 인기 게시글 조회 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> findTopPostsFilteredByCategory(
            @PathVariable("dormitory-category") String dormNum
    ) {

        if(!dormNum.equals("DORM1") && !dormNum.equals("DORM2") && !dormNum.equals("DORM3")) {
            throw new NotFoundException("올바른 기숙사 분류가 아닙니다.");
        }

        List<Post> posts = postService.findTopPosts(dormNum);

        if (posts.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(DefaultResponseDto.builder()
                            .responseCode("NO_CONTENT")
                            .responseMessage("인기 게시글이 없습니다.")
                            .build());
        }

        List<PostDefaultResponseDto> response = posts.stream()
                .map(post -> new PostDefaultResponseDto(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 인기 게시글 조회 완료")
                        .data(response)
                        .build()
                );
    }

    // TODO: 최신순 혹은 과거순으로 order by 해서 반환
    @GetMapping("/member/posts/me/write")
    @ApiOperation(value = "Post 로그인한 멤버가 작성한 모든 게시글 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 로그인한 멤버가 작성한 게시글 조회 완료"),
            @ApiResponse(code = 204, message = "작성한 게시글이 없습니다."),
            @ApiResponse(code = 401, message = "로그인이 필요합니다."),
            @ApiResponse(code = 500, message = "멤버가 작성한 게시글 전체 조회 중 서버 에러가 발생했습니다.")
    })
    public ResponseEntity<DefaultResponseDto<Object>> findPostsByMember(
            HttpServletRequest request2
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        List<Post> posts = postService.findPostsByMember(member);

        if(posts.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(DefaultResponseDto.builder()
                            .responseCode("NO_CONTENT")
                            .responseMessage("작성한 게시글이 없습니다.")
                            .build());
        }

        List<PostDefaultResponseDto> response = posts.stream()
                .map(post -> new PostDefaultResponseDto(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 로그인한 멤버가 작성한 게시글 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @GetMapping("/member/posts/me/liked")
    @ApiOperation(value = "Post 로그인한 멤버가 공감한 모든 게시글 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 로그인한 멤버가 공감한 게시글 조회 완료"),
            @ApiResponse(code = 204, message = "공감한 게시글이 없습니다."),
            @ApiResponse(code = 401, message = "로그인이 필요합니다."),
            @ApiResponse(code = 500, message = "멤버가 공감한 게시글 전체 조회 중 서버 에러가 발생했습니다.")
    })
    public ResponseEntity<DefaultResponseDto<Object>> findLikedPostsByMember(
            HttpServletRequest request2
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        List<Long> foundPostIds = postLikedMemberService.findLikedPostIdsByMemberId(loginMemberId);

        if(foundPostIds.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(DefaultResponseDto.builder()
                            .responseCode("NO_CONTENT")
                            .responseMessage("공감한 게시글이 없습니다.")
                            .build());
        }

        List<Post> resultPosts = new ArrayList<>();

        for(Long postId : foundPostIds) {
            Post post = postService.findById(postId);
            resultPosts.add(post);
        }

        List<PostDefaultResponseDto> response = resultPosts.stream()
                .map(post -> new PostDefaultResponseDto(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 로그인한 멤버가 공감한 게시글 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @GetMapping("/member/posts/{postId}/liked")
    @ApiOperation(value = "Post 특정 게시글을 공감한 멤버 카운트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 특정 게시글을 공감한 멤버 카운트 조회 완료"),
            @ApiResponse(code = 401, message = "로그인이 필요합니다."),
            @ApiResponse(code = 404, message = "게시글이 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Post 특정 게시글을 공감한 멤버 조회 중 서버 에러가 발생했습니다.")
    })
    public ResponseEntity<DefaultResponseDto<Object>> findLikedMembersByPost(
            HttpServletRequest request2, @PathVariable("postId") Long postId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        List<Long> foundMemberIds = postLikedMemberService.findLikedMemberIdsByPostId(postId);

        int likedCount = foundMemberIds.size();

        PostLikedMembersCountByPostResponseDto response = PostLikedMembersCountByPostResponseDto.builder()
                .postId(post.getId())
                .likedCounts(likedCount)
                .build();

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 특정 게시글을 공감한 멤버 카운트 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @PutMapping("/member/post/{postId}/liked")
    @ApiOperation(value = "Post 게시글 공감하기")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 게시글 공감하기 완료"),
            @ApiResponse(code = 401, message = "로그인이 필요합니다."),
            @ApiResponse(code = 404, message = "공감할 게시글이 존재하지 않습니다."),
            @ApiResponse(code = 409, message = "본인이 작성한 게시글에 공감 불가 OR 이미 공감한 게시글에는 공감 불가"),
            @ApiResponse(code = 500, message = "Post 게시글 공감하기 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> savePostLikes(
            HttpServletRequest request2, @PathVariable("postId") Long postId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        postService.addPostLikes(member, post);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 게시글 공감하기 완료")
                        .build()
                );
    }

    @DeleteMapping("/member/post/{postId}/liked")
    @ApiOperation(value = "Post 게시글 공감 취소하기")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 게시글 공감 삭제 완료"),
            @ApiResponse(code = 401, message = "로그인이 필요합니다."),
            @ApiResponse(code = 404, message = "멤버가 게시글에 공감하지 않았습니다."),
            @ApiResponse(code = 500, message = "Post 게시글 공감 취소하기 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> deletePostLikes(
            HttpServletRequest request2, @PathVariable("postId") Long postId
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);
        Post post = postService.findById(postId);

        postService.deletePostLikes(member, post);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 게시글 공감 삭제 완료")
                        .build()
                );
    }

    @DeleteMapping("/member/post/{postId}")
    @ApiOperation(value = "Post 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 삭제 완료"),
            @ApiResponse(code = 401, message = "로그인 필요 혹은 삭제 권한 없는 멤버"),
            @ApiResponse(code = 500, message = "Post 삭제 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> deletePost(
            HttpServletRequest request2, @PathVariable("postId") Long postId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        postService.deletePost(postId,member);
        // TODO: 댓글, 대댓글 삭제 처리 필요

        return ResponseEntity.status(204)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 삭제 완료")
                        .build()
                );
    }
}
