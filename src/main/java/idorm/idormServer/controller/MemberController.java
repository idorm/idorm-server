package idorm.idormServer.controller;

import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.domain.Member;
import idorm.idormServer.exceptions.http.ConflictException;
import idorm.idormServer.exceptions.http.NotFoundException;
import idorm.idormServer.jwt.JwtTokenProvider;
import idorm.idormServer.service.EmailService;
import idorm.idormServer.service.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static idorm.idormServer.dto.MemberDTO.*;

@RestController
@RequiredArgsConstructor
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
            @ApiResponse(code = 401, message = "권한 없음, 로그인 필요"),
    })
    @ExceptionHandler
    @GetMapping("/member")
    public ResponseEntity<DefaultResponseDto<Object>> memberOne(
            HttpServletRequest request
    ) {
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(userPk);
        MemberOneDto memberOneDTO = new MemberOneDto(member);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 단건 조회 완료")
                        .data(new Result(memberOneDTO))
                        .build());
    }

    /**
     * 멤버 등록
     */
    @ApiOperation(value = "멤버 회원가입", notes = "회원가입은 이메일 인증이 완료된 후가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member 회원 가입 성공"),
            @ApiResponse(code = 400, message = "Member 회원 가입 실패"),
            @ApiResponse(code = 500, message = "Member 회원 가입 중 에러")
    }
    )
    @PostMapping("/register")
    public ResponseEntity<DefaultResponseDto<Object>> saveMember(@RequestBody @Valid CreateMemberRequest request) throws Exception {

        if(emailService.findByEmailOp(request.getEmail()).isEmpty()) {
            throw new IllegalStateException("등록되지 않은 이메일입니다.");
        }

        if(request.getPassword().isEmpty()) {
            throw new IllegalStateException("비밀번호 입력은 필수입니다.");
        }

        emailService.findByEmail(request.getEmail()).isJoined();
        memberService.join(request.getEmail(), passwordEncoder.encode(request.getPassword()));

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
    @PatchMapping("/member")
    public ResponseEntity<DefaultResponseDto<Object>> updateMember(
            HttpServletRequest request2, @RequestBody @Valid UpdateMemberRequest request) {
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));

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
                throw new IllegalStateException("이미 존재하는 닉네임입니다");
            }
        }
    }

    /**
     * 멤버 업데이트(비밀번호)
     */
    @ApiOperation(value = "멤버의 (비밀번호) 변경", notes = "멤버의 비밀번호만 입력하고싶을 때 사용합니다.")
    @PatchMapping("/member/password")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberPassword(
            HttpServletRequest request2, @RequestBody @Valid UpdateMemberPasswordRequest request) {

        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));

        memberService.updatePassword(userPk, passwordEncoder.encode(request.getPassword()));

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 비밀번호 업데이트 완료")
                        .data(new ReturnMemberIdResponse(userPk))
                        .build());
    }

    /**
     * 멤버 업데이트(닉네임)
     */
    @ApiOperation(value = "멤버의 (닉네임) 변경", notes = "멤버의 닉네임만 변경하고 싶을 때 사용합니다.")
    @PatchMapping("/member/nickname")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberNickname(
            HttpServletRequest request2, @RequestBody @Valid UpdateMemberNicknameRequest request) {

        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));

        if(!(request.getNickname().isEmpty())) { // 입력받은 닉네임이 있다면

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

                    throw new IllegalStateException("변경할 닉네임을 입력해주세요.");
                }
                else { // 저장된 멤버의 닉네임과 입력받은 닉네임이 다르다면
                    throw new IllegalStateException("이미 존재하는 닉네임입니다");
                }
            }
        } else { // 입력받은 닉네임이 없다면
            throw new IllegalStateException("닉네임 입력은 필수입니다.");
        }
    }


    /**
     * 맴버삭제
     */
    @ApiOperation(value = "멤버 삭제", notes = "멤버를 삭제하면 가입된 이메일도 삭제합니다.")
    @DeleteMapping("/member")
    public ResponseEntity<DefaultResponseDto<Object>> deleteMember(
            HttpServletRequest request
    ) {
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));
        String email = memberService.findById(userPk).getEmail();
        emailService.deleteById(emailService.findByEmail(email).getId());
        memberService.deleteMember(userPk);
//        return new DeleteMember(userPk);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 삭제 완료")
                        .data(new DeleteMember(userPk))
                        .build());
    }

    /**
     * 로그인
     */
    @ApiOperation(value = "로그인", notes = "로그인 후 토큰을 던져줍니다.")
    @PostMapping("/login")
    public ResponseEntity<DefaultResponseDto<Object>> login(@RequestBody LoginMemberRequest member) {
        Member mem = memberService.findByEmail(member.getEmail())
                .orElseThrow(() -> new ConflictException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(member.getPassword(), mem.getPassword())) {
            throw new ConflictException("잘못된 비밀번호입니다.");}

        Iterator<String> iter = mem.getRoles().iterator();
        List<String> roles=new ArrayList<>();
        while (iter.hasNext()) {
            roles.add(iter.next());
        }

//        return jwtTokenProvider.createToken(mem.getUsername(), roles);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("로그인 완료, 토큰을 반환합니다.")
                        .data(jwtTokenProvider.createToken(mem.getUsername(), roles))
                        .build());
    }

    /**
     * admin role
     */

    /**
     * 전체 멤버 조회(관리자)
     */
    @ApiOperation(value = "관리자용 - 전체 멤버 조회", notes = "가입된 전체 멤버에 대한 데이터를 조회할 수 있습니다.")
    @GetMapping("/admin/members")
    public ResponseEntity<DefaultResponseDto<Object>> members() {
        List<Member> members = memberService.findAll();
        List<MemberOneDto> collect = members.stream()
                .map(o -> new MemberOneDto(o)).collect(Collectors.toList());


        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("전체 Member를 조회합니다.")
                        .data(new Result(collect))
                        .build());
    }

    /**
     * 멤버 업데이트(관리자)
     */
    @ApiOperation(value = "관리자용 - 특정 멤버 정보 수정(비밀번호, 닉네임)")
    @PatchMapping("/admin/member/{id}")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberRoot(
            @PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {
        memberService.updateMember(id,passwordEncoder.encode(request.getPassword()), request.getNickname());
//        return new ReturnMemberIdResponse(id);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage(id + " 아이디를 가진 Member의 비밀번호 혹은 닉네임을 수정합니다.")
                        .data("id: " + id + " | " + "닉네임: " + memberService.findById(id).getNickname())
                        .build());
    }

    /**
     * 멤버 삭제(관리자)
     */
    @ApiOperation(value = "관리자용 - 특정 멤버 삭제")
    @DeleteMapping("/admin/member/{id}")
    public ResponseEntity<DefaultResponseDto<Object>> deleteMemberRoot(
            @PathVariable("id") Long id
    ) {
        String email = memberService.findById(id).getEmail();
        emailService.deleteById(emailService.findByEmail(email).getId());
        memberService.deleteMember(id);
//        return new DeleteMember(id);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage(id + " 아이디를 가진 Member 삭제 완료")
                        .data(new DeleteMember(id))
                        .build());
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
