package idorm.idormServer.member.controller;

import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.service.EmailService;
import idorm.idormServer.exceptions.http.ConflictException;
import idorm.idormServer.exceptions.http.UnauthorizedException;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.dto.*;
import idorm.idormServer.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Api(tags = "Member API")
@PropertySource("classpath:login.properties")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${id}")
    private String ENV_USERNAME;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "Member 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member 단건 조회 완료"),
            @ApiResponse(code = 401, message = "로그인한 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Member 단건 조회 중 서버 에러 발생")
    })
    @GetMapping("/member")
    public ResponseEntity<DefaultResponseDto<Object>> findOneMember(
            HttpServletRequest request
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));

        Member member = memberService.findById(loginMemberId);

        MemberDefaultResponseDto response = MemberDefaultResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .matchingInfoId(member.getMatchingInfo().getId())
                .build();

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 단건 조회 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "Member 회원가입", notes = "회원가입은 이메일 인증이 완료된 후 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member 회원가입 완료"),
            @ApiResponse(code = 400, message = "입력은 필수입니다."),
            @ApiResponse(code = 401, message = "등록되지 않은 이메일입니다."),
            @ApiResponse(code = 500, message = "이미 존재하는 회원입니다.")
    }
    )
    @PostMapping("/register")
    public ResponseEntity<DefaultResponseDto<Object>> saveMember(@RequestBody @Valid MemberSaveRequestDto request) {

        Optional<Email> emailOp = emailService.findByEmailOp(request.getEmail());

        if(emailOp.isEmpty()) {
            throw new UnauthorizedException("등록하지 않은 이메일입니다.");
        }

        Long createdMemberId = memberService.save(request.getEmail(), passwordEncoder.encode(request.getPassword()));
        Member newMember = memberService.findById(createdMemberId);
        emailService.updateIsJoined(emailOp.get().getEmail());

        MemberDefaultResponseDto response = MemberDefaultResponseDto.builder()
                .id(newMember.getId())
                .email(newMember.getEmail())
                .build();

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 회원가입 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "로그인 가능 시, Member 비밀번호 변경")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member 로그인 상태에서 비밀번호 변경 완료"),
            @ApiResponse(code = 400, message = "비밀번호 입력은 필수입니다."),
            @ApiResponse(code = 401, message = "비밀번호를 변경할 멤버를 찾을 수 없습니다."),
            @ApiResponse(code = 500, message = "Member 비밀번호 변경 중 서버 에러 발생")
    }
    )
    @PatchMapping("/member/password")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberPasswordLogin(
            HttpServletRequest request2, @RequestBody @Valid MemberUpdatePasswordStatusLoginRequestDto request) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));

        memberService.updatePassword(loginMemberId, passwordEncoder.encode(request.getPassword()));

        Member member = memberService.findById(loginMemberId);

        emailService.updateIsJoined(member.getEmail());

        MemberDefaultResponseDto response = MemberDefaultResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .matchingInfoId(member.getMatchingInfo().getId())
                .build();

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 로그인 상태에서 비밀번호 변경 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "로그인 불가 시, Member 비밀번호 변경")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member 로그아웃 상태에서 비밀번호 변경 완료"),
            @ApiResponse(code = 400, message = "입력은 필수입니다."),
            @ApiResponse(code = 401, message = "등록되지 않은 이메일입니다."),
            @ApiResponse(code = 404, message = "비밀번호를 변경할 멤버를 찾을 수 없습니다."),
            @ApiResponse(code = 500, message = "Member 비밀번호 변경 중 서버 에러 발생")
    }
    )
    @PatchMapping("/password")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberPasswordLogout(
            @RequestBody @Valid MemberUpdatePasswordStatusLogoutRequestDto request) {


        emailService.findByEmail(request.getEmail());

        Member foundMember = memberService.findByEmail(request.getEmail());

        memberService.updatePassword(foundMember.getId(), passwordEncoder.encode(request.getPassword()));
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
            @ApiResponse(code = 200, message = "Member 닉네임 변경 완료"),
            @ApiResponse(code = 400, message = "기존의 닉네임과 같습니다."),
            @ApiResponse(code = 401, message = "UnAuthorized"),
            @ApiResponse(code = 409, message = "이미 존재하는 닉네임입니다."),
            @ApiResponse(code = 500, message = "Member 비밀번호 변경 중 서버 에러 발생")
    }
    )
    @PatchMapping("/member/nickname")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberNickname(
            HttpServletRequest request2, @RequestBody @Valid MemberUpdateNicknameRequestDto request) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);

        Optional<Member> foundMemberByNickname = memberService.findByNickname(request.getNickname());

        if(foundMemberByNickname.isEmpty()) {
            memberService.updateNickname(loginMemberId, request.getNickname());

            MemberDefaultResponseDto response = new MemberDefaultResponseDto(loginMember);

            return ResponseEntity.status(200)
                    .body(DefaultResponseDto.builder()
                            .responseCode("OK")
                            .responseMessage("Member 닉네임 변경 완료")
                            .data(response)
                            .build());
        }

        if(loginMember.equals(foundMemberByNickname.get())) {
            throw new ConflictException("기존의 닉네임과 같습니다.");
        } else {
            throw new ConflictException("이미 존재하는 닉네임입니다.");
        }
    }

    @ApiOperation(value = "Member 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member 삭제 완료"),
            @ApiResponse(code = 401, message = "등록되지 않은 이메일입니다."),
            @ApiResponse(code = 404, message = "삭제할 멤버를 찾을 수 없습니다."),
            @ApiResponse(code = 500, message = "Member 삭제 중 서버 에러 발생")
    }
    )
    @DeleteMapping("/member")
    public ResponseEntity<DefaultResponseDto<Object>> deleteMember(
            HttpServletRequest request
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));
        String email = memberService.findById(loginMemberId).getEmail();
        Email emailObj = emailService.findByEmail(email);

        //TODO: Member 삭제 시, 연관된 엔티티들 전부 삭제가 필요합니다.
        emailService.deleteById(emailObj.getId());
        memberService.deleteMember(loginMemberId);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 삭제 완료")
                        .build());
    }

    @ApiOperation(value = "Member 로그인", notes = "로그인 후 토큰을 던져줍니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member 로그인 완료, 토큰을 반환합니다."),
            @ApiResponse(code = 400, message = "가입되지 않은 이메일입니다."),
            @ApiResponse(code = 400, message = "이메일 입력은 필수입니다."),
            @ApiResponse(code = 400, message = "비밀번호 입력은 필수입니다."),
            @ApiResponse(code = 401, message = "올바르지 않은 비밀번호입니다.")
    }
    )
    @PostMapping("/login")
    public ResponseEntity<DefaultResponseDto<Object>> login(@RequestBody MemberLoginRequestDto request) {

        Member loginMember = memberService.findByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), loginMember.getPassword())) {
            throw new UnauthorizedException("올바르지 않은 비밀번호입니다.");
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
            @ApiResponse(code = 200, message = "Member 전체 조회 완료"),
            @ApiResponse(code = 401, message = "UnAuthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "조회할 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Member 전체 조회 중 서버 에러 발생")
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
            @ApiResponse(code = 200, message = "Member 정보 수정 완료"),
            @ApiResponse(code = 401, message = "UnAuthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "해당 id의 멤버가 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "Member 비밀번호 변경 중 서버 에러 발생")
    }
    )
    @PatchMapping("/admin/member/{id}")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberRoot(
            @PathVariable("id") Long updateMemberId, @RequestBody @Valid MemberUpdateStatusAdminRequestDto request) {

        Member updateMember = memberService.findById(updateMemberId);

        memberService.updatePassword(updateMemberId, request.getPassword());
        memberService.updateNickname(updateMemberId, request.getNickname());

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
            @ApiResponse(code = 200, message = "Member 삭제 완료"),
            @ApiResponse(code = 401, message = "UnAuthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "삭제할 멤버를 찾을 수 없습니다."),
            @ApiResponse(code = 500, message = "Member 삭제 중 서버 에러 발생")
    }
    )
    @DeleteMapping("/admin/member/{id}")
    public ResponseEntity<DefaultResponseDto<Object>> deleteMemberRoot(
            @PathVariable("id") Long deleteMemberId
    ) {

        String email = memberService.findById(deleteMemberId).getEmail();
        Email emailObj = emailService.findByEmail(email);

        //TODO: Member 삭제 시, 연관된 엔티티들 전부 삭제가 필요합니다.
        emailService.deleteById(emailObj.getId());
        memberService.deleteMember(deleteMemberId);

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

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
