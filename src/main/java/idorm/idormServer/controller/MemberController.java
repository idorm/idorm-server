package idorm.idormServer.controller;

import idorm.idormServer.domain.Member;
import idorm.idormServer.jwt.JwtTokenProvider;
import idorm.idormServer.service.EmailService;
import idorm.idormServer.service.MemberService;
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
     * 전체 멤버 조회(관리자)
     */
    @GetMapping("/admin/members")
    public Result members() {
        List<Member> members = memberService.findAll();
        List<MemberOneDto> collect = members.stream()
                .map(o -> new MemberOneDto(o)).collect(Collectors.toList());
        return new Result(collect);
    }

    /**
     * 멤버 등록
     */
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
     * 멤버업데이트(비밀번호변경) (관리자)
     */
    @PatchMapping("/admin/member/{id}")
    public ReturnMemberIdResponse updateMemberRoot(
            @PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {
        memberService.updateMember(id,passwordEncoder.encode(request.getPassword()));
        return new ReturnMemberIdResponse(id);
    }

    /**
     * 멤버 삭제(관리자)
     */
    @DeleteMapping("/admin/member/{id}")
    public DeleteMember deleteMemberRoot(
            @PathVariable("id") Long id
    ) {
        String email = memberService.findById(id).getEmail();
        emailService.deleteById(emailService.findByEmail(email).getId());
        memberService.deleteMember(id);
        return new DeleteMember(id);
    }

    /**
     * 맴버업데이트(비밀번호변경)
     */
    @PatchMapping("/member")
    public ReturnMemberIdResponse updateMember(
            HttpServletRequest request2, @RequestBody @Valid UpdateMemberRequest request) {
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        if(memberService.findByNickname(request.getNickname()).isEmpty()) { // 저장된 닉네임이 없다면
            memberService.updateMember(userPk,passwordEncoder.encode(request.getPassword()));
            return new ReturnMemberIdResponse(userPk);
        }
        else { // 닉네임이 존재하면
            if(memberService.findById(userPk).getNickname().equals(request.getNickname())) { // 유저 본인의 닉네임가 그대로 일치할 시
                memberService.updateMember(userPk,passwordEncoder.encode(request.getPassword()));
                return new ReturnMemberIdResponse(userPk);
            }
            else { // 유저 본인의 닉네임과 같지 않은데 존재하는 경우
                throw new IllegalArgumentException("이미 존재하는 닉네임입니다");
            }
        }
    }

    /**
     * 맴버삭제
     */
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


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }



}
