package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Embeddable;

@Embeddable //JPA 내장타입
@Getter //Setter를 제공하지 않음 -> 변경 불가
public class Address {
    private String city;
    private String street;
    private String zipcode;

    //JPA 스펙상 기본적으로 만드는 생성자. 쓸일없음
    protected Address(){

    }

    //초기에 생성할 때만 값을 설정할 수 있게 하도록 생성자 생성
    public Address(String city, String street, String zipcode){
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;

    }
}
