package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") //테이블 이름 변경
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter @Setter
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    private String title;

    //ManyToOne은 디폴트가 EAGER이므로 지연 로딩으로 설정
    @ManyToOne(fetch=FetchType.LAZY)  //멤버 : 주문 = 1 : N,
    @JoinColumn(name = "member_id")
    private Member member;


    //OneToMany는 기본 FetchType이 LAZY임
    //Cascade 설정
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();


    //OneToOne도 디폴트가 EAGER이므로 지연 로딩으로 설정
    //1:1 연관관계 주인으로 Order, 엑세스를 많이 하는 쪽을 주인으로 두는것이 좋음
    @OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //spring boot hibername는 order_date로 컬럼명이 바뀜
    private LocalDateTime orderDate;    //주문시간, Hibernate가 지원

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 ENUM


//    //초기에 생성할 때만 값을 설정할 수 있게 하도록 생성자 생성
//    public Order(){
//
//    }
    public Order(String title, LocalDateTime orderDate, OrderStatus status){
        this.title = title;
        this.orderDate = orderDate;
        this.status = status;

    }

    // == 연관관계 메서드 ==
    public void setMamber(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);

    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }


    /* 도메인 모델 패턴!! 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것
    * <-> 트랜잭션 스크립트 패턴 : 서비스에서 대부분의 비즈니스 로직을 처리하는 것
    * */
    // 생성 로직

    /**
     * 주문 생성 로직
     */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem: orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비즈니스 로직
    /**
     * 주문 취소
     */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능");
        }
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem: orderItems){
            orderItem.cancel();
        }
    }

    // 조회 로직
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem: orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;

//        return orderItems.stream()
//                .mapToInt(OrderItem::getTotalPrice)
//                .sum();
    }

}
