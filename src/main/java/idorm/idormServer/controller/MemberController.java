package idorm.idormServer.controller;

import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.domain.Member;
import idorm.idormServer.jwt.JwtTokenProvider;
import idorm.idormServer.service.EmailService;
import idorm.idormServer.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static idorm.idormServer.dto.MemberDTO.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "멤버 API")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 멤버조회
     */
    @ApiOperation(value = "로그인된 멤버 단건 조회", notes = "로그인이 되어있어야 합니다. 멤버의 id, nickname, email을 조회 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "로그인된 Member 단건 조회 성공"),
            @ApiResponse(code = 401, message = "UnAuthorized")
    })
    @GetMapping("/member")
    public ResponseEntity<DefaultResponseDto<Object>> memberOne(
            HttpServletRequest request
    ) {
        try {
            long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));
            Member member = memberService.findById(userPk);
            MemberOneDto memberOneDTO = new MemberOneDto(member);

            return ResponseEntity.status(200)
                    .body(DefaultResponseDto.builder()
                            .responseCode("OK")
                            .responseMessage("Member 단건 조회 완료")
                            .data(new Result(memberOneDTO))
                            .build());
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "멤버 단건 조회 중에 서버 에러가 발생했습니다.");
        }
    }

    /**
     * 멤버 등록
     */
    @ApiOperation(value = "멤버 회원가입", notes = "회원가입은 이메일 인증이 완료된 후가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "회원 가입 완료"),
            @ApiResponse(code = 400, message = "등록되지 않은 이메일입니다."),
            @ApiResponse(code = 400, message = "비밀번호 입력은 필수입니다."),
            @ApiResponse(code = 500, message = "회원가입 중에 서버 에러가 발생했습니다.")
    }
    )
    @PostMapping("/register")
    public ResponseEntity<DefaultResponseDto<Object>> saveMember(@RequestBody @Valid CreateMemberRequest request) {

        if(emailService.findByEmailOp(request.getEmail()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "등록되지 않은 이메일입니다.");
        }

        if(request.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호 입력은 필수입니다.");
        }

        try {
            emailService.findByEmail(request.getEmail()).isJoined();
            memberService.join(request.getEmail(), passwordEncoder.encode(request.getPassword()));
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입 중에 서버 에러가 발생했습니다.");
        }

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("회원 가입 완료")
                        .data(new ReturnMemberIdResponse(
                                memberService.join(request.getEmail(), passwordEncoder.encode(request.getPassword()))))
                        .build());
    }

    /**
     * 맴버업데이트(비밀번호, 닉네임변경)
     */
    @ApiOperation(value = "멤버의 (비밀번호, 닉네임) 변경", notes = "'비밀번호와 닉네임' 둘 다 업데이트할 때 사용합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member 비밀번호, 닉네임 업데이트 완료"),
            @ApiResponse(code = 200, message = "Member 비밀번호 업데이트 완료"),
            @ApiResponse(code = 400, message = "이미 존재하는 닉네임입니다."),
            @ApiResponse(code = 401, message = "UnAuthorized"),
            @ApiResponse(code = 500, message = "비밀번호, 닉네임 변경 중에 서버 에러가 발생했습니다.")
    }
    )
    @PatchMapping("/member")
    public ResponseEntity<DefaultResponseDto<Object>> updateMember(
            HttpServletRequest request2, @RequestBody @Valid UpdateMemberRequest request) {
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));

        try {
            if(memberService.findByNickname(request.getNickname()).isEmpty()) { // 입력한 닉네임을 가진 멤버가 db에 없다면

                memberService.updateMember(userPk,passwordEncoder.encode(request.getPassword()), request.getNickname()); // 비밀번호, 닉네임 업데이트

                return ResponseEntity.status(200)
                        .body(DefaultResponseDto.builder()
                                .responseCode("OK")
                                .responseMessage("Member 비밀번호, 닉네임 업데이트 완료")
                                .data(new ReturnMemberIdResponse(userPk))
                                .build());
            }
            else { // 입력한 닉네임을 가진 멤버가 db에 있다면
                if(memberService.findById(userPk).getNickname().equals(request.getNickname())) { // 저장된 멤버의 닉네임과 입력받은 닉네임이 같다면

                    memberService.updateMember(userPk,passwordEncoder.encode(request.getPassword()), request.getNickname()); // 비밀번호 업데이트

                    return ResponseEntity.status(200)
                            .body(DefaultResponseDto.builder()
                                    .responseCode("OK")
                                    .responseMessage("Member 비밀번호 업데이트 완료")
                                    .data(new ReturnMemberIdResponse(userPk))
                                    .build());
                }
                else { // 저장된 멤버의 닉네임과 입력받은 닉네임이 다르다면
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다.");
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "비밀번호, 닉네임 변경 중에 서버 에러가 발생했습니다.");
        }
    }

    /**
     * 멤버 업데이트(비밀번호)
     */
    @ApiOperation(value = "멤버의 (비밀번호) 변경", notes = "멤버의 비밀번호만 입력하고싶을 때 사용합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member 비밀번호 업데이트 완료"),
            @ApiResponse(code = 401, message = "UnAuthorized"),
            @ApiResponse(code = 500, message = "비밀번호 변경 중에 서버 에러가 발생했습니다.")
    }
    )
    @PatchMapping("/member/password")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberPassword(
            HttpServletRequest request2, @RequestBody @Valid UpdateMemberPasswordRequest request) {

        try {
            long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));

            memberService.updatePassword(userPk, passwordEncoder.encode(request.getPassword()));

            return ResponseEntity.status(200)
                    .body(DefaultResponseDto.builder()
                            .responseCode("OK")
                            .responseMessage("Member 비밀번호 업데이트 완료")
                            .data(new ReturnMemberIdResponse(userPk))
                            .build());
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "비밀번호 변경 중에 서버 에러가 발생했습니다.");
        }
    }

    /**
     * 멤버 업데이트(닉네임)
     */
    @ApiOperation(value = "멤버의 (닉네임) 변경", notes = "멤버의 닉네임만 변경하고 싶을 때 사용합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member 닉네임 업데이트 완료"),
            @ApiResponse(code = 400, message = "변경할 닉네임을 입력해주세요."),
            @ApiResponse(code = 400, message = "이미 존재하는 닉네임입니다."),
            @ApiResponse(code = 401, message = "UnAuthorized"),
            @ApiResponse(code = 500, message = "비밀번호 변경 중에 서버 에러가 발생했습니다.")
    }
    )
    @PatchMapping("/member/nickname")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberNickname(
            HttpServletRequest request2, @RequestBody @Valid UpdateMemberNicknameRequest request) {

        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        System.out.println(userPk);

//        if(memberService.findById(userPk) == null) {
//            new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
//        }

//        if(request.getNickname().isEmpty() || request.getNickname().isBlank()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "닉네임 입력은 필수입니다.");
//        }

        if(memberService.findByNickname(request.getNickname()).isEmpty()) { // 입력한 닉네임을 가진 멤버가 db에 없다면

            memberService.updateNickname(userPk, request.getNickname()); // 닉네임 업데이트

            return ResponseEntity.status(200)
                    .body(DefaultResponseDto.builder()
                            .responseCode("OK")
                            .responseMessage("Member 닉네임 업데이트 완료")
                            .data("id: " + new ReturnMemberIdResponse(userPk))
                            .build());


        } else { // 입력한 닉네임을 가진 멤버가 db에 있다면
            if(memberService.findById(userPk).getNickname().equals(request.getNickname())) { // 저장된 멤버의 닉네임과 입력받은 닉네임이 같다면

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "변경할 닉네임을 입력해주세요.");
            }
            else { // 저장된 멤버의 닉네임과 입력받은 닉네임이 다르다면
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다.");
            }
        }
    }


    /**
     * 맴버삭제
     */
    @ApiOperation(value = "멤버 삭제", notes = "멤버를 삭제하면 가입된 이메일도 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member 탈퇴유저로 설정 완료"),
            @ApiResponse(code = 401, message = "UnAuthorized"),
            @ApiResponse(code = 500, message = "멤버를 탈퇴 유저로 설정 중에 서버 에러가 발생했습니다.")
    }
    )
    @DeleteMapping("/member")
    public ResponseEntity<DefaultResponseDto<Object>> deleteMember(
            HttpServletRequest request
    ) {
        try {
            long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));
            String email = memberService.findById(userPk).getEmail();

            emailService.deleteById(emailService.findByEmail(email).getId());
            memberService.deleteMember(userPk);

            return ResponseEntity.status(200)
                    .body(DefaultResponseDto.builder()
                            .responseCode("OK")
                            .responseMessage("Member 탈퇴유저로 설정 완료")
                            .data(new DeleteMember(userPk))
                            .build());
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "멤버를 탈퇴 유저로 설정 중에 서버 에러가 발생했습니다.");
        }
    }

    /**
     * 로그인
     */
    @ApiOperation(value = "로그인", notes = "로그인 후 토큰을 던져줍니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "로그인 완료, 토큰을 반환합니다."),
            @ApiResponse(code = 400, message = "가입되지 않은 이메일입니다."),
            @ApiResponse(code = 400, message = "잘못된 비밀번호입니다."),
            @ApiResponse(code = 500, message = "로그인 중에 서버 에러가 발생했습니다")
    }
    )
    @PostMapping("/login")
    public ResponseEntity<DefaultResponseDto<Object>> login(@RequestBody LoginMemberRequest member) {
        try {
            Member mem = memberService.findByEmail(member.getEmail())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "가입되지 않은 이메일입니다."));

            if (!passwordEncoder.matches(member.getPassword(), mem.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다.");}


            Iterator<String> iter = mem.getRoles().iterator();
            List<String> roles=new ArrayList<>();
            while (iter.hasNext()) {
                roles.add(iter.next());
            }

            return ResponseEntity.status(200)
                    .body(DefaultResponseDto.builder()
                            .responseCode("OK")
                            .responseMessage("로그인 완료, 토큰을 반환합니다.")
                            .data(jwtTokenProvider.createToken(mem.getUsername(), roles))
                            .build());
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "로그인 중에 서버 에러가 발생했습니다.");
        }
    }

    /**
     * admin role
     */

    /**
     * 전체 멤버 조회(관리자)
     */
    @ApiOperation(value = "관리자용 - 전체 멤버 조회", notes = "가입된 전체 멤버에 대한 데이터를 조회할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "전체 Member를 조회합니다."),
            @ApiResponse(code = 401, message = "UnAuthorized"),
            @ApiResponse(code = 403, message = "Forbidden"), // TODO : 에러 확인
            @ApiResponse(code = 500, message = "관리자용으로 전체 Member를 조회하는 중에 서버 에러가 발생했습니다.")
    }
    )
    @GetMapping("/admin/members")
    public ResponseEntity<DefaultResponseDto<Object>> members() {
        try {
            List<Member> members = memberService.findAll();
            List<MemberOneDto> collect = members.stream()
                    .map(o -> new MemberOneDto(o)).collect(Collectors.toList());


            return ResponseEntity.status(200)
                    .body(DefaultResponseDto.builder()
                            .responseCode("OK")
                            .responseMessage("전체 Member를 조회합니다.")
                            .data(new Result(collect))
                            .build());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "관리자용으로 전체 Member를 조회하는 중에 서버 에러가 발생했습니다.");
        }
    }

    /**
     * 멤버 업데이트(관리자)
     */
    @ApiOperation(value = "관리자용 - 특정 멤버 정보 수정(비밀번호, 닉네임)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "관리자용으로 Member의 비밀번호 혹은 닉네임을 수정합니다."),
            @ApiResponse(code = 401, message = "UnAuthorized"),
            @ApiResponse(code = 403, message = "Forbidden"), // TODO : 에러 확인
            @ApiResponse(code = 500, message = "관리자용으로 Member의 비밀번호 혹은 닉네임을 수정 중에 서버 에러가 발생했습니다.")
    }
    )
    @PatchMapping("/admin/member/{id}")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberRoot(
            @PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {
        try {

            memberService.updateMember(id,passwordEncoder.encode(request.getPassword()), request.getNickname());

            return ResponseEntity.status(200)
                    .body(DefaultResponseDto.builder()
                            .responseCode("OK")
                            .responseMessage("관리자용으로 Member의 비밀번호 혹은 닉네임을 수정합니다.")
                            .data("id: " + id + " | " + "닉네임: " + memberService.findById(id).getNickname())
                            .build());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "관리자용으로 Member의 비밀번호 혹은 닉네임을 수정 중에 서버 에러가 발생했습니다.");
        }
    }

    /**
     * 멤버 삭제(관리자)
     */
    @ApiOperation(value = "관리자용 - 특정 멤버 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "관리자용으로 Member를 탈퇴유저로 설정합니다."),
            @ApiResponse(code = 401, message = "UnAuthorized"),
            @ApiResponse(code = 403, message = "Forbidden"), // TODO : 에러 확인
            @ApiResponse(code = 500, message = "관리자용으로 Member를 탈퇴유저로 설정 중에 서버 에러가 발생했습니다.")
    }
    )
    @DeleteMapping("/admin/member/{id}")
    public ResponseEntity<DefaultResponseDto<Object>> deleteMemberRoot(
            @PathVariable("id") Long id
    ) {
        try {
            String email = memberService.findById(id).getEmail();
            emailService.deleteById(emailService.findByEmail(email).getId());
            memberService.deleteMember(id);

            return ResponseEntity.status(200)
                    .body(DefaultResponseDto.builder()
                            .responseCode("OK")
                            .responseMessage("관리자용으로 Member를 탈퇴유저로 설정합니다.")
                            .data(new DeleteMember(id))
                            .build());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "관리자용으로 Member를 탈퇴유저로 설정 중에 서버 에러가 발생했습니다.");
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
