package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;

// 어노테이션 사용 : 컴포넌트 스캔으로 스프링 빈으로 관리됨
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //Spring이 Injection
//    @PersistenceContext
    private final EntityManager em;

    //생성자 Injection을 사용하면 @PersistenceContext -> @Autowired로 대체 가능
    //-> @RequiredArgsConstructor로 생성자 Injection 자동완성가능
//    public MemberRepository(EntityManager em){
//        this.em = em;
//    }

    // 팩토리 주입 시(별로 안쓰임 팩토리는)
    // @PersistenceUnit
    //private EntityFactory emf

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        // qlstring은 Entity를 대상으로 하는 JPQL(SQL과 다름)
        List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();
        return result;
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}



