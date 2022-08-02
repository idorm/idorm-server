package idorm.idormServer.controller;

import idorm.idormServer.domain.Member;
import idorm.idormServer.dto.MemberDTO;
import idorm.idormServer.dto.MemberDTO.CreateMemberResponse;
import idorm.idormServer.dto.MemberDTO.DeleteMember;
import idorm.idormServer.service.EmailService;
import idorm.idormServer.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;



}
