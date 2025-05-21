package nhn.academy.controller;

import nhn.academy.model.*;
import nhn.academy.model.annotation.Auth;
import nhn.academy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/name")
    public String getName(){
        return "최정환";
    }

    @GetMapping("/me")
    public MemberEntity getMe(){
        return new MemberEntity();
    }

    // 멤버 등록
    @PostMapping("/members")
    public ResponseEntity addMember(@RequestBody MemberCreateCommand memberCreateCommand,
                                    @Auth Requester requester){
        memberService.createMember(memberCreateCommand);
        return ResponseEntity.ok().build();
    }

    // 멤버 전체 조회
    @GetMapping("/members")
    public List<MemberEntity> getMembers(){
        return memberService.getMembers();
    }

    // 멤버 단건 조회
    @GetMapping("/members/{memberId}")
    public MemberEntity getMembers(@PathVariable String memberId){
        return memberService.getMember(memberId);
    }

    // 수정
    @PutMapping("/members/{memberId}")
    public MemberEntity updateMember(@PathVariable String memberId, @RequestBody MemberCreateCommand mcc) {
        return memberService.updateMember(memberId,mcc);
    }
    // 삭제
    @DeleteMapping("/members/{memberId}")
    public ResponseEntity deleteMember(@PathVariable String memberId){
        memberService.removeMember(memberId);
        return ResponseEntity.ok().build();
    }
}
