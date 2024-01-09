package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {
    @Autowired
    EntityManager em;
    @Test
    public void updateTest() throws Exception {
        Book book = em.find(Book.class, 18L);

        //TX
        book.setName("asdf");

        //변경감지 == Dirty Checking
        //바꿔놓으면 커밋시점에 반영됨
        //TX Commit




    }

}
