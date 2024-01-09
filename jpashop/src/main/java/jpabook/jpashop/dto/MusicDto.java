package jpabook.jpashop.dto;

import jpabook.jpashop.domain.test.Music;
import lombok.Data;

@Data
public class MusicDto {
    private Long id;
    private String title;
    private Long producerId;
    private String producerName;

    public MusicDto(Music music){
        id = music.getId();
        title = music.getTitle();
        producerId = music.getProducer().getId();
        producerName = music.getProducer().getName();
    }

    public MusicDto(Long id, String title, Long producerId, String producerName){
        this.id = id;
        this.title = title;
        this.producerId = producerId;
        this.producerName = producerName;

    }
}


