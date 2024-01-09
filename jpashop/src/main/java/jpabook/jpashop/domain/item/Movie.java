package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("M")    //싱글 테이블일 때 구분방법
public class Movie extends Item{
    private String director;
    private String actor;
}
