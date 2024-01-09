package jpabook.jpashop.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.test.Music;
import jpabook.jpashop.dto.MusicDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MusicRepository {
    private final EntityManager em;


    public List<Music> findAll() {
        return em.createQuery("select m from Music m join fetch m.producer p", Music.class
        ).getResultList();
    }

    public List<MusicDto> findAllDto() {
        return em.createQuery("select new jpabook.jpashop.dto.MusicDto(m.id, m.title, p.id, p.name) " +
                "from Music m join m.producer p",
                MusicDto.class
        ).getResultList();
    }
}
