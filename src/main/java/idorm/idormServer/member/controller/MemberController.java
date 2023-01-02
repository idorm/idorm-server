package idorm.idormServer.member.controller;

import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.service.EmailService;
import idorm.idormServer.exceptions.CustomException;
import idorm.idormServer.exceptions.ErrorResponse;

import idorm.idormServer.matching.service.DislikedMemberService;
import idorm.idormServer.matching.service.LikedMemberService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.dto.*;
import idorm.idormServer.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static idorm.idormServer.exceptions.ErrorCode.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "Member API")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;

    private final LikedMemberService likedMemberService;
    private final DislikedMemberService dislikedMemberService;

    @Value("${DB_USERNAME}")
    private String ENV_USERNAME;

    @Value("${ADMIN_PASSWORD}")
    private String ENV_PASSWORD;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "Member 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/member")
    public ResponseEntity<DefaultResponseDto<Object>> findOneMember(
            HttpServletRequest request
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        MemberDefaultResponseDto response = new MemberDefaultResponseDto(member);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 단건 조회 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "Member 회원가입", notes = "회원가입은 이메일 인증이 완료된 후 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / EMAIL_FORMAT_INVALID / PASSWORD_FORMAT_INVALID / " +
                            "NICKNAME_FORMAT_INVALID",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "EMAIL_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409",
                    description = "DUPLICATE_NICKNAME",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    @PostMapping("/register")
    public ResponseEntity<DefaultResponseDto<Object>> saveMember(
            @RequestBody @Valid MemberSaveRequestDto request
    ) {

        Email foundEmail = emailService.findByEmail(request.getEmail());

        passwordValidator(request.getPassword());
        nicknameValidator(request.getNickname());

        Long createdMemberId = memberService.save(request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname());

        Member newMember = memberService.findById(createdMemberId);
        emailService.updateIsJoined(foundEmail.getEmail());

        MemberDefaultResponseDto response = new MemberDefaultResponseDto(newMember);

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 회원가입 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "Member 프로필 사진 저장")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FILE_SIZE_EXCEEDED",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "FILE_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    @PostMapping("/member/profile-photo")
    public ResponseEntity<DefaultResponseDto<Object>> saveMemberProfilePhoto(
            HttpServletRequest request2, @RequestPart(value = "file", required = false) MultipartFile photo) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);

        if(photo == null) {
            throw new CustomException(FILE_NOT_FOUND);
        }

        memberService.saveProfilePhoto(loginMemberId, photo);

        MemberDefaultResponseDto response = new MemberDefaultResponseDto(loginMember);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 프로필 사진 저장 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "Member 프로필 사진 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "FILE_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/member/profile-photo")
    public ResponseEntity<DefaultResponseDto<Object>> deleteMemberProfilePhoto(
            HttpServletRequest request2) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);

        memberService.deleteMemberProfilePhoto(loginMember);

        return ResponseEntity.status(204)
                .body(DefaultResponseDto.builder()
                        .responseCode("NO_CONTENT")
                        .responseMessage("Member 프로필 사진 삭제 완료")
                        .build());
    }

    @ApiOperation(value = "로그인 가능 시, Member 비밀번호 변경")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / PASSWORD_FORMAT_INVALID",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    @PatchMapping("/member/password")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberPasswordLogin(
            HttpServletRequest request2, @RequestBody @Valid MemberUpdatePasswordStatusLoginRequestDto request) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        passwordValidator(request.getPassword());

        memberService.updatePassword(member, passwordEncoder.encode(request.getPassword()));
        emailService.updateIsJoined(member.getEmail());

        MemberDefaultResponseDto response = new MemberDefaultResponseDto(member);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 로그인 상태에서 비밀번호 변경 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "로그인 불가 시, Member 비밀번호 변경")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / EMAIL_FORMAT_INVALID / PASSWORD_FORMAT_INVALID",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "EMAIL_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    @PatchMapping("/password")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberPasswordLogout(
            @RequestBody @Valid MemberUpdatePasswordStatusLogoutRequestDto request) {


        emailService.findByEmail(request.getEmail());

        Member foundMember = memberService.findByEmail(request.getEmail());

        passwordValidator(request.getPassword());

        memberService.updatePassword(foundMember, passwordEncoder.encode(request.getPassword()));
        emailService.updateIsJoined(foundMember.getEmail());

        MemberDefaultResponseDto response = new MemberDefaultResponseDto(foundMember);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 로그아웃 상태에서 비밀번호 변경 완료")
                        .data(response)
                        .build());

    }

    @ApiOperation(value = "Member 닉네임 변경")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / NICKNAME_FORMAT_INVALID",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409",
                    description = "DUPLICATE_NICKNAME / CANNOT_UPDATE_NICKNAME",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    @PatchMapping("/member/nickname")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberNickname(
            HttpServletRequest request2, @RequestBody @Valid MemberUpdateNicknameRequestDto request) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);

        nicknameValidator(request.getNickname());
        memberService.updateNickname(loginMember, request.getNickname());

        MemberDefaultResponseDto response = new MemberDefaultResponseDto(loginMember);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 닉네임 변경 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "Member 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/member")
    public ResponseEntity<DefaultResponseDto<Object>> deleteMember(
            HttpServletRequest request
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member foundMember = memberService.findById(loginMemberId);

        likedMemberService.deleteLikedMembers(foundMember.getId());
        dislikedMemberService.deleteDislikedMembers(foundMember.getId());
        memberService.deleteMember(foundMember);

        return ResponseEntity.status(204)
                .body(DefaultResponseDto.builder()
                        .responseCode("NO_CONTENT")
                        .responseMessage("Member 삭제 완료")
                        .build());
    }


    @ApiOperation(value = "Member 로그인", notes = "로그인 후 토큰을 던져줍니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "PASSWORD_FORMAT_INVALID / EMAIL_FORMAT_INVALID / FIELD_REQUIRED",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "EMAIL_NOT_FOUND / MEMBER_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409",
                    description = "UNAUTHORIZED_PASSWORD",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    @PostMapping("/login")
    public ResponseEntity<DefaultResponseDto<Object>> login(
            @RequestBody @Valid MemberLoginRequestDto request) {

        Member loginMember = null;

        passwordValidator(request.getPassword());

        if(request.getEmail().equals(ENV_USERNAME + "@inu.ac.kr")) {
            if (!passwordEncoder.matches(request.getPassword(), passwordEncoder.encode(ENV_PASSWORD))) {
                throw new CustomException(UNAUTHORIZED_PASSWORD);
            }
            loginMember = memberService.findById(1L);
        } else {
            loginMember = memberService.findByEmail(request.getEmail());

            if (!passwordEncoder.matches(request.getPassword(), loginMember.getPassword())) {
                throw new CustomException(UNAUTHORIZED_PASSWORD);
            }
        }

        Iterator<String> iter = loginMember.getRoles().iterator();
        List<String> roles = new ArrayList<>();

        while (iter.hasNext()) {
            roles.add(iter.next());
        }

        String createdToken = jwtTokenProvider.createToken(loginMember.getUsername(), roles);
        MemberDefaultResponseDto response = new MemberDefaultResponseDto(loginMember, createdToken);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 로그인 완료, 토큰을 반환합니다.")
                        .data(response)
                        .build());
    }

    /**
     * admin role
     */

    @ApiOperation(value = "관리자용 / Member 전체 조회", notes = "가입된 전체 멤버에 대한 데이터를 조회할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_AUTHORIZATION",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    @GetMapping("/admin/members")
    public ResponseEntity<DefaultResponseDto<Object>> members() {

        List<Member> members = memberService.findAll();
        List<MemberDefaultResponseDto> collect = members.stream()
                .map(o -> new MemberDefaultResponseDto(o)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 전체 조회 완료")
                        .data(new Result(collect))
                        .build());
    }

    @ApiOperation(value = "관리자용 / 특정 Member 정보 수정 (비밀번호, 닉네임)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "PASSWORD_FORMAT_INVALID / NICKNAME_FORMAT_INVALID / FIELD_REQUIRED",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_AUTHORIZATION",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409",
                    description = "DUPLICATE_NICKNAME",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    @PatchMapping("/admin/member/{id}")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberRoot(
            @PathVariable("id") Long updateMemberId, @RequestBody @Valid MemberUpdateStatusAdminRequestDto request) {

        Member updateMember = memberService.findById(updateMemberId);

        passwordValidator(request.getPassword());
        nicknameValidator(request.getNickname());

        memberService.updatePassword(updateMember, request.getPassword());
        memberService.updateNicknameByAdmin(updateMember, request.getNickname());

        MemberDefaultResponseDto response = new MemberDefaultResponseDto(updateMember);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 정보 수정 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "관리자용 / 특정 Member 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_AUTHORIZATION",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    @DeleteMapping("/admin/member/{id}")
    public ResponseEntity<DefaultResponseDto<Object>> deleteMemberRoot(
            @PathVariable("id") Long deleteMemberId
    ) {
        Member foundMember = memberService.findById(deleteMemberId);
        likedMemberService.deleteLikedMembers(foundMember.getId());
        dislikedMemberService.deleteDislikedMembers(foundMember.getId());
        memberService.deleteMember(foundMember);

        List<Member> members = memberService.findAll();
        List<MemberDefaultResponseDto> collect = members.stream()
                .map(o -> new MemberDefaultResponseDto(o)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 삭제 완료")
                        .data(new Result(collect))
                        .build());
    }

    private void passwordValidator(String password) {

        /**
         * 비밀번호 포맷 확인
         * 8 - 15자, 필수 영소문자, 숫자, 특수문자
         * optional 대문자
         */
        final int MIN = 8;
        final int MAX = 20;

        final String REGEX =
                "^((?=.*\\d)(?=.*[a-zA-Z])(?=.*[\\W]).{" + MIN + "," + MAX + "})$";

        // 숫자, 특수문자가 포함되어야 한다.
        final String REGEX_SYMBOL =
                "([0-9].*[!,@,#,^,&,*,(,)])|([!,@,#,^,&,*,(,)].*[0-9])";

        Pattern pattern = Pattern.compile(REGEX);
        Pattern pattern_symbol = Pattern.compile(REGEX_SYMBOL);

        Matcher matcher = pattern.matcher(password);
        Matcher matcher_symbol = pattern_symbol.matcher(password);

        if(!matcher.find() || !matcher_symbol.find()) { // wrong regex
            throw new CustomException(PASSWORD_FORMAT_INVALID);
        }
    }

    private void nicknameValidator(String nickname) {
        /**
         * 닉네임 포맷 확인
         * 한글 / 영어 / 숫자 입력 가능
         * 2 - 8자, 공백 불가
         */
        final String REGEX = "^[A-Za-z0-9ㄱ-ㅎ가-힣]{2,10}$";

        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(nickname);

        if (!matcher.find()) {
            throw new CustomException(NICKNAME_FORMAT_INVALID);
        }

    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
