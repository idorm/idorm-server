package idorm.idormServer.controller;

import idorm.idormServer.domain.Member;
import idorm.idormServer.jwt.JwtTokenProvider;
import idorm.idormServer.service.EmailService;
import idorm.idormServer.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
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
    @ApiOperation(value = "로그인된 멤버의 데이터 조회", notes = "로그인이 되어있어야 합니다. 멤버의 id, nickname, email을 조회 가능합니다.")
    @GetMapping("/member")
    public Result memberOne(
            HttpServletRequest request
    ) {
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(userPk);
        MemberOneDto memberOneDTO = new MemberOneDto(member);
        return new Result(memberOneDTO);
    }

    /**
     * 멤버 등록
     */
    @ApiOperation(value = "멤버 회원가입", notes = "회원가입은 이메일 인증이 완료된 후가능합니다.")
    @PostMapping("/register")
    public ReturnMemberIdResponse saveMember(@RequestBody @Valid CreateMemberRequest request) {

        if(emailService.findByEmailOp(request.getEmail()).isEmpty()) {
            throw new NoSuchElementException();
        } else {
            emailService.findByEmail(request.getEmail()).isJoined();
            return new ReturnMemberIdResponse(
                    memberService.join(request.getEmail(), passwordEncoder.encode(request.getPassword())));
        }
    }

    /**
     * 맴버업데이트(비밀번호, 닉네임변경)
     */
    @ApiOperation(value = "멤버의 (비밀번호, 닉네임) 변경", notes = "비밀번호 업데이트 혹은 닉네임 업데이트 혹은 (비밀번호,닉네임) 업데이트에 사용합니다. 닉네임은 null 값을 허용하기 때문에 아직 입력되지 않았다면 request에는 password에 대해서만 적어야 합니다. 또한 비밀번호를 업데이트하지 않는 경우에도 기존의 비밀번호를 입력해주어야 합니다.")
    @PatchMapping("/member")
    public ReturnMemberIdResponse updateMember(
            HttpServletRequest request2, @RequestBody @Valid UpdateMemberRequest request) {
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));

        if(memberService.findByNickname(request.getNickname()).isEmpty()) { // 입력한 닉네임을 가진 멤버가 db에 없다면
            memberService.updateMember(userPk,passwordEncoder.encode(request.getPassword()), request.getNickname()); // 비밀번호, 닉네임 업데이트
            return new ReturnMemberIdResponse(userPk);
        }
        else { // 입력한 닉네임을 가진 멤버가 db에 있다면
            if(memberService.findById(userPk).getNickname().equals(request.getNickname())) { // 저장된 멤버의 닉네임과 입력받은 닉네임이 같다면

                memberService.updateMember(userPk,passwordEncoder.encode(request.getPassword()), request.getNickname()); // 비밀번호 업데이트
                return new ReturnMemberIdResponse(userPk);
            }
            else { // 저장된 멤버의 닉네임과 입력받은 닉네임이 다르다면
                throw new IllegalArgumentException("이미 존재하는 닉네임입니다");
            }
        }
    }

    /**
     * 맴버삭제
     */
    @ApiOperation(value = "멤버 삭제", notes = "멤버를 삭제하면 가입된 이메일도 삭제합니다.")
    @DeleteMapping("/member")
    public DeleteMember deleteMember(
            HttpServletRequest request
    ) {
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request.getHeader("X-AUTH-TOKEN")));
        String email = memberService.findById(userPk).getEmail();
        emailService.deleteById(emailService.findByEmail(email).getId());
        memberService.deleteMember(userPk);
        return new DeleteMember(userPk);
    }

    /**
     * 로그인
     */
    @ApiOperation(value = "로그인", notes = "로그인 후 토큰을 던져줍니다.")
    @PostMapping("/login")
    public String login(@RequestBody LoginMemberRequest member) {
        Member mem = memberService.findByEmail(member.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(member.getPassword(), mem.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");}
        Iterator<String> iter = mem.getRoles().iterator();
        List<String> roles=new ArrayList<>();
        while (iter.hasNext()) {
            roles.add(iter.next());
        }
        return jwtTokenProvider.createToken(mem.getUsername(), roles);
    }

    /**
     * admin role
     */

    /**
     * 전체 멤버 조회(관리자)
     */
    @ApiOperation(value = "관리자용 - 전체 멤버 조회", notes = "가입된 전체 멤버에 대한 데이터를 조회할 수 있습니다.")
    @GetMapping("/admin/members")
    public Result members() {
        List<Member> members = memberService.findAll();
        List<MemberOneDto> collect = members.stream()
                .map(o -> new MemberOneDto(o)).collect(Collectors.toList());
        return new Result(collect);
    }

    /**
     * 멤버 업데이트(관리자)
     */
    @ApiOperation(value = "관리자용 - 특정 멤버 정보 수정(비밀번호, 닉네임)")
    @PatchMapping("/admin/member/{id}")
    public ReturnMemberIdResponse updateMemberRoot(
            @PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {
        memberService.updateMember(id,passwordEncoder.encode(request.getPassword()), request.getNickname());
        return new ReturnMemberIdResponse(id);
    }

    /**
     * 멤버 삭제(관리자)
     */
    @ApiOperation(value = "관리자용 - 특정 멤버 삭제")
    @DeleteMapping("/admin/member/{id}")
    public DeleteMember deleteMemberRoot(
            @PathVariable("id") Long id
    ) {
        String email = memberService.findById(id).getEmail();
        emailService.deleteById(emailService.findByEmail(email).getId());
        memberService.deleteMember(id);
        return new DeleteMember(id);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
