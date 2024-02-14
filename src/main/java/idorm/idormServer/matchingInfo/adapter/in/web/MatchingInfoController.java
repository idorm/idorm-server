//package idorm.idormServer.matchingInfo.controller;
//
//import idorm.idormServer.common.dto.DefaultResponseDto;
//import idorm.idormServer.common.exception.CustomException;
//import idorm.idormServer.matchingInfo.domain.MatchingInfo;
//import idorm.idormServer.matchingInfo.dto.MatchingInfoIsPublicRequest;
//import idorm.idormServer.matchingInfo.dto.MatchingInfoRequest;
//import idorm.idormServer.matchingInfo.dto.MatchingInfoResponse;
//import idorm.idormServer.matchingInfo.service.MatchingInfoService;
//import idorm.idormServer.member.domain.Member;
//import idorm.idormServer.member.service.MemberService;
//import idorm.idormServer.support.token.JwtTokenProvider;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//
//import static idorm.idormServer.common.exception.ExceptionCode.DUPLICATE_MATCHINGINFO;
//
//@Tag(name = "6. MatchingInfo", description = "온보딩 api")
//@Validated
//@RestController
//@RequestMapping("/api/v1")
//@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
//public class MatchingInfoController {
//
//    private final MatchingInfoService matchingInfoService;
//    private final MemberService memberService;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Operation(summary = "온보딩 정보 생성", description = "- 최초로 온보딩 정보를 저장할 경우만 사용 가능합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "201", description = "MATCHINGINFO_SAVED",
//                    content = @Content(schema = @Schema(implementation = MatchingInfoResponse.class))),
//            @ApiResponse(responseCode = "400",
//                    description = "FIELD_REQUIRED / *_CHARACTER_INVALID / *_LENGTH_INVALID"),
//            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
//            @ApiResponse(responseCode = "409", description = "DUPLICATE_MATCHINGINFO"),
//            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
//    })
//    @PostMapping("/member/matchinginfo")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<DefaultResponseDto<Object>> save(
//            HttpServletRequest servletRequest,
//            @RequestBody @Valid MatchingInfoRequest request) {
//
//        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
//        Member member = memberService.findById(memberId);
//
//        matchingInfoService.validateMBTI(request.getMbti().toUpperCase());
//
//        if(matchingInfoService.findByMemberOp(member).isPresent()) {
//            throw new CustomException(null, DUPLICATE_MATCHINGINFO);
//        }
//
//        MatchingInfoResponse response = new MatchingInfoResponse(matchingInfoService.save(request.toEntity(member)));
//
//        return ResponseEntity.status(201)
//                .body(DefaultResponseDto.builder()
//                        .responseCode("MATCHINGINFO_SAVED")
//                        .responseMessage("온보딩 저장 완료")
//                        .data(response)
//                        .build()
//                );
//    }
//
//    @PutMapping("/member/matchinginfo")
//    @Operation(summary = "온보딩 정보 수정")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200", description = "MATCHINGINFO_UPDATED",
//                    content = @Content(schema = @Schema(implementation = MatchingInfoResponse.class))),
//            @ApiResponse(responseCode = "400",
//                    description = "FIELD_REQUIRED / *_CHARACTER_INVALID / *_LENGTH_INVALID"),
//            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
//            @ApiResponse(responseCode = "404", description = "MATCHINGINFO_NOT_FOUND"),
//            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
//    })
//    public ResponseEntity<DefaultResponseDto<Object>> updateMatchingInfo(
//            HttpServletRequest servletRequest,
//            @RequestBody @Valid MatchingInfoRequest request) {
//
//        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
//        Member member = memberService.findById(loginMemberId);
//
//        matchingInfoService.validateMBTI(request.getMbti().toUpperCase());
//
//        MatchingInfo matchingInfo = matchingInfoService.findByMemberId(member.getId());
//
//        matchingInfoService.updateMatchingInfo(matchingInfo, request);
//        MatchingInfoResponse response = new MatchingInfoResponse(matchingInfo);
//
//        return ResponseEntity.status(200)
//                .body(DefaultResponseDto.builder()
//                        .responseCode("MATCHINGINFO_UPDATED")
//                        .responseMessage("MatchingInfo 수정 완료")
//                        .data(response)
//                        .build()
//                );
//    }
//
//    @Operation(summary = "온보딩 정보 공개 여부 수정")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "ISMATCHINGINFOPUBLIC_UPDATED"),
//            @ApiResponse(responseCode = "400", description = "FIELD_REQUIRED"),
//            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
//            @ApiResponse(responseCode = "404", description = "MATCHINGINFO_NOT_FOUND"),
//            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
//    })
//    @PatchMapping("/member/matchinginfo")
//    public ResponseEntity<DefaultResponseDto<Object>> updateisMatchingInfoPublic(
//            HttpServletRequest servletRequest,
//            MatchingInfoIsPublicRequest request) {
//
//        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
//        Member member = memberService.findById(memberId);
//
//        matchingInfoService.updateMatchingInfoIsPublic(matchingInfoService.findByMemberId(member.getId()),
//                request.getIsMatchingInfoPublic());
//
//        return ResponseEntity.status(200)
//                .body(DefaultResponseDto.builder()
//                        .responseCode("ISMATCHINGINFOPUBLIC_UPDATED")
//                        .responseMessage("MatchingInfo 공개여부 수정 완료")
//                        .build()
//                );
//    }
//
//    @GetMapping("/member/matchinginfo")
//    @Operation(summary = "온보딩 정보 단건 조회")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200", description = "MATCHINGINFO_FOUND",
//                    content = @Content(schema = @Schema(implementation = MatchingInfoResponse.class))),
//            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
//            @ApiResponse(responseCode = "404", description = "MATCHINGINFO_NOT_FOUND"),
//            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
//    })
//    public ResponseEntity<DefaultResponseDto<Object>> findMatchingInfo(
//            HttpServletRequest servletRequest) {
//
//        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
//        Member member = memberService.findById(memberId);
//
//        MatchingInfoResponse response = new MatchingInfoResponse(matchingInfoService.findByMemberId(member.getId()));
//
//        return ResponseEntity.status(200)
//                .body(DefaultResponseDto.builder()
//                        .responseCode("MATCHINGINFO_FOUND")
//                        .responseMessage("MatchingInfo 단건 조회")
//                        .data(response)
//                        .build()
//                );
//    }
//
//    @DeleteMapping("/member/matchinginfo")
//    @Operation(summary = "온보딩 정보 삭제")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200", description = "MATCHINGINFO_DELETED"),
//            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
//            @ApiResponse(responseCode = "404", description = "MATCHINGINFO_NOT_FOUND"),
//            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
//    })
//    public ResponseEntity<DefaultResponseDto<Object>> deleteMatchingInfo(HttpServletRequest request2) {
//
//        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader(AUTHENTICATION_HEADER_NAME)));
//        Member member = memberService.findById(loginMemberId);
//
//        matchingInfoService.delete(matchingInfoService.findByMemberId(member.getId()));
//
//        return ResponseEntity.status(200)
//                .body(DefaultResponseDto.builder()
//                        .responseCode("MATCHINGINFO_DELETED")
//                        .responseMessage("MatchingInfo 삭제 완료")
//                        .build()
//                );
//    }
//}
