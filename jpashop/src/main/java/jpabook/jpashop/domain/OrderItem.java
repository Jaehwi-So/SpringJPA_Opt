package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access= AccessLevel.PROTECTED)   //    protected OrderItem(), 임의로 생성하지 못하도록, 생성 메서드를 이용해서만 생성 가능하도록 제약
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore //JSON(Response)에 포함이 되지 않도록 함
    private Order order;
    private int orderPrice; //주문 당시 가격
    private int count; //주문 당시 수량


    //생성 로직
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);    //재고 줄임
        return orderItem;
    }
    //비즈니스 로직
    /**
     * 주문 취소
     */
    public void cancel() {
        getItem().addStock(count);  //재고수량 원상복구
    }

    //조회 로직
    /**
     * 총 금액 계싼
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();    //가격 * 수량
    }
}
