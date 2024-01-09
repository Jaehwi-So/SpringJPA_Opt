package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id") //PK 이름 지정
    private Long id;

    @NotEmpty
    private String name;

    @Embedded   //내장 타입임을 명시
    private Address address;



    @JsonIgnore //JSON(Response)에 포함이 되지 않도록 함
    @OneToMany(mappedBy = "member") //Order에서 member에 의해서 매핑된 미러일 뿐임을 명시(연관관계의 주체가 아님)
    private List<Order> orders = new ArrayList<>();




}
