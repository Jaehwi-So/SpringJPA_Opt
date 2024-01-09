package jpabook.jpashop.api;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

//@Controller @ResponseBody
@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers(); //엔티티를 직접 반환하면 엔티티의 모든 필요없는 것까지 전부 노출됨 + 엔티티가 변경되면 사양이 변경됨
    }

    @GetMapping("/api/v2/members")
    public Result membersV2(){
        List<Member> findMembers =  memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName())) //MemberDTO로 생성
                .collect(Collectors.toList()); //List로 변환
        return new Result(collect);
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){ //DTO로 만들어야 한다. 엔티티를 노출해서도 안됨
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    //DTO 사용
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid
                                               UpdateMemberRequest request){
        memberService.update(id, request.getName());
        Member findMember = memberService.findMember(id);  //커멘드와 쿼리를 분리하는 스타일
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }


    @Data
    static class CreateMemberRequest{
        @NotEmpty
        private String name;
    }

    @Data
    static class UpdateMemberRequest{
        @NotEmpty
        private String name;
    }


    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }


    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    @AllArgsConstructor //생성자 자동
    static class UpdateMemberResponse{
        private Long id;
        private String name;

    }


}
