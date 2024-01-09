package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//기본적으로 트랜잭션 안에서 데이터를 변경해야 하며, LazyLoad 등도 가능해진다.
//readonly 사용 시 조회시의 성능을 올려줌, 그러나 쓰기가 안됨
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    // @Autowired
    // final을 적용시키면 생성자 Injection에서 주입이 안될 시의 오류를 방지할수 있음
    private final MemberRepository memberRepository;

    // Setter Injection을 통해서 주입, 테스트 코드같은 곳에서 유용. 그러나 런타임 환경에서 변경할 이유도 없고 변경 위험이 있음 -> 안좋음
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    // 따라서 생성자 Injection으로 MemberRepository를 주입하는 것이 좋음, 요즘에는 @AutoWired가 없어도 스프링이 자동으로 Injection을 지원함
    // -> Lombok의 @RequiredArgsContructor 어노테이션이 자동으로 만들어줌
//    @Autowired
//    public MemberService(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     * @param member
     * @return
     */

    @Transactional  //readonly=false인 transaction
    public Long join(Member member){
        //중복 회원
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public void update(Long id, String name){
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원");
        }
    }

    /**
     * 회원 전체 조회
     * @return
     */

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    /**
     * 회원 단일 조회
     * @param memberId
     * @return
     */
    public Member findMember(Long memberId){
        return memberRepository.findOne(memberId);
    }


}
