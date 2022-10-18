package idorm.idormServer.community.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.dto.post.PostDefaultRequestDto;
import idorm.idormServer.community.dto.post.PostDefaultResponseDto;
import idorm.idormServer.community.service.PostService;
import idorm.idormServer.community.vo.SavePostVo;
import idorm.idormServer.exceptions.http.InternalServerErrorException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "Community_Post(게시글) API")
public class PostController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PostService postService;

    @PostMapping(value = "/post",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Post 저장")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 저장 완료"),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Post save 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> savePost(
            @ModelAttribute @Valid SavePostVo postRequest
    ) throws IOException {

        // TODO: 로그아웃 상태이면 막아야함
        // TODO: files 개수 10개 제한
        // TODO: memberId가 현재 로그인 계정 아니면 예외처리 필요
        // TODO: 기숙사 분류는 DORM1, DORM2, DORM3 만 받아야함

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

    @PutMapping("/member/post/${id}")
    @ApiOperation(value = "Post 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 수정 완료"),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Post update 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> updatePost(
            HttpServletRequest request2,
            @PathVariable("id") Long updatePostId,
            @RequestBody @Valid PostDefaultRequestDto postRequest
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        Post foundPost = postService.findById(updatePostId);

        postService.updatePost(updatePostId,
                member,
                postRequest.getTitle(),
                postRequest.getContent(),
                postRequest.getIsAnonymous());

        PostDefaultResponseDto response = new PostDefaultResponseDto(foundPost);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 수정 완료")
                        .data(response)
                        .build()
                );
    }

    @GetMapping("/member/posts/{dormitory-category}")
    @ApiOperation(value = "Post 기숙사 카테고리로 필터링된 게시글 다건 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 다건 조회 완료"),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Post 다건 조회 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> findPostsFilteredByCategory(
            @RequestParam(value = "dormitory-category", required = false, defaultValue = "DORM1") String dormNum
    ) {

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

    @GetMapping("/member/post/${id}")
    @ApiOperation(value = "Post 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 단건 조회 완료"),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Post 단건 조회 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> findOnePost(
            @PathVariable("id") Long postId
    ) {


        // TODO: Post 단건 조회 로직 - 댓글, 대댓글까지 조회
        Post foundPost = postService.findById(postId);

        PostDefaultResponseDto response = new PostDefaultResponseDto(foundPost);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 단건 조회 완료")
                        .data(response)
                        .build()
                );
    }

    /**
     * 기숙사 카테고리 별 인기 게시글 다건 조회
     */
    @GetMapping("/member/posts/{dormitory-category}/top")
    @ApiOperation(value = "Post 기숙사 카테고리로 필터링된 인기 게시글 다건 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 인기 게시글 조회 완료"),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Post 인기 게시글 조회 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> findTopPostsFilteredByCategory(
            @PathVariable("dormitory-category") String dormNum
    ) {

        // TODO: Post 인기 게시글 다건 조회 로직
//        List<Post> posts = postService.findTopPostsFilteredByCategory(dormNum);

//        List<PostDefaultResponseDto> response = posts.stream()
//                .map(post -> new PostDefaultResponseDto(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 인기 게시글 조회 완료")
//                        .data(response)
                        .build()
                );
    }

    /**
     * 로그인한 멤버가 작성한 모든 게시글 리스트 조회
     */
    @GetMapping("/member/posts/me")
    @ApiOperation(value = "Post 로그인한 멤버가 작성한 모든 게시글 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 다건 조회 완료"),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Post 다건 조회 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> findPostsByMember(
            HttpServletRequest request2
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        // TODO: Post 로그인한 멤버가 작성한 모든 게시글 리스트 조회
//        List<Post> posts = postService.findPostsByMember(member);

//        List<PostDefaultResponseDto> response = posts.stream()
//                .map(post -> new PostDefaultResponseDto(post)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 단건 조회 완료")
//                        .data(response)
                        .build()
                );
    }

    @DeleteMapping("/member/post/${id}")
    @ApiOperation(value = "Post 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post 삭제 완료"),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Post 삭제 중 서버 에러 발생"),
    })
    public ResponseEntity<DefaultResponseDto<Object>> deletePost(
            HttpServletRequest request2, @PathVariable("id") Long postId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        postService.deletePost(postId,member);
        // TODO: 댓글/대댓글도 isVisible 처리

        return ResponseEntity.status(204)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Post 삭제 완료")
                        .build()
                );
    }
}
