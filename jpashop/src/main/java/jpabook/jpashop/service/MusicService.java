package jpabook.jpashop.service;


import jakarta.persistence.LockModeType;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.test.Music;
import jpabook.jpashop.dto.MusicDto;
import jpabook.jpashop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j  //Logger
public class MusicService {
    private final MusicRepository musicRepository;

    public List<Music> findMusics(){
        return musicRepository.findAll();
    }

    public List<MusicDto> findMusicsV2(){
        return musicRepository.findAllDto();
    }
}
