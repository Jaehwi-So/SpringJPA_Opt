package jpabook.jpashop.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Getter @Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(fetch=FetchType.LAZY, mappedBy = "delivery")
    @JsonIgnore //JSON(Response)에 포함이 되지 않도록 함
    private Order order;

    @Embedded
    private Address address;

    // @Enumerated(EnumType.ORDINAL) Ordinal이 디폴트로 지정되는데, 추가되면 다 밀리므로 절대 사용 X!!!
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;  //READY, COMP
}
