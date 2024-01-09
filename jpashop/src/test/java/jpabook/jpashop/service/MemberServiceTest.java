package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    //@Rollback(false)
    public void 회원가입() throws Exception {
        //given (주어졌을 때)
        Member member = new Member();
        member.setName("jung");

        //when (실행하면)
        Long saveId = memberService.join(member);

        //then (결과가 이게 나와야함)
        em.flush(); //Insert문을 볼 수 있으며, 롤백이 됨
        assertEquals(member, memberRepository.findOne(saveId));

    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given (주어졌을 때)
        Member member = new Member();
        member.setName("재휘");

        Member member2 = new Member();
        member2.setName("재휘");

        //when (실행하면)
        memberService.join(member);
        memberService.join(member2);    //예외가 발생해야 함 -> 어노테이션에 성공 케이스 예외 추가
//        try{
//            memberService.join(member2);    //예외가 발생해야 함
//        } catch(IllegalStateException e){
//            return;
//        }


        //then (결과가 이게 나와야함)
        fail("예외가 발생해야 한다");    //Assert에서 제공하는 fail


    }

}