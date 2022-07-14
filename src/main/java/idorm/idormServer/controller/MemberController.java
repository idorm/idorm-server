package idorm.idormServer.controller;

import idorm.idormServer.domain.Member;
import idorm.idormServer.dto.MemberDTO;
import idorm.idormServer.dto.MemberDTO.CreateMemberResponse;
import idorm.idormServer.dto.MemberDTO.DeleteMember;
import idorm.idormServer.dto.MemberDTO.MemberOneDTO;
import idorm.idormServer.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
class MemberController {

    private final MemberService memberService;

    /**
     * 멤버 조회
     */
    @GetMapping("/member/{id}")
    public Result memberOne(
            @PathVariable("id") Long id
    ) {
        Member member = memberService.findOne(id);
        MemberOneDTO memberOneDTO = new MemberOneDTO(member);
        return new Result(memberOneDTO);
    }

    /**
     * 멤버 등록
     */
    @PostMapping("/member")
    public CreateMemberResponse saveMember(@RequestBody @Valid MemberDTO.CreateMemberRequest request) {
        return new CreateMemberResponse(
                memberService.join(request.getEmail(), request.getPassword()));
    }

    /**
     * 멤버 삭제
     */
//    @DeleteMapping("/member/{id}")
//    public DeleteMember deleteMember(
//            @PathVariable("id") Long id
//    ) {
//
//
//    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
