package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item){
        //JPA는 Item에 넣기 전까지는 ID가 없음 -> 신규값
        if(item.getId() == null){
            em.persist((item));
        }
        //DB에 등록된 값을 가져온 것
        else{
            em.merge(item); //Update와 비슷한 Merge. 준영속 컨텍스트를 영속성 컨텍스트 객체로 변환하여 반환해준다.
            // (파라미터인 item이 변환되는 것은 아니고 새로운 영속성 객체가 반환됨)

        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public Item findOne(Long id, LockModeType lock){
        return em.find(Item.class, id, lock);
    }


    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
