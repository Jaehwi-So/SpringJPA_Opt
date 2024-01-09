package jpabook.jpashop.repository.query;


import jakarta.persistence.EntityManager;
import jpabook.jpashop.dto.OrderFlatDto;
import jpabook.jpashop.dto.OrderItemQueryDto;
import jpabook.jpashop.dto.OrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;


    public List<OrderQueryDto> findOrderQueryDtos(){
        List<OrderQueryDto> result = findOrders();

        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return result;
    }

    public List<OrderQueryDto> findAllByDtoOptimization() {
        List<OrderQueryDto> result = findOrders();

        List<Long> orderIds = result.stream()
                        .map(o -> o.getOrderId())
                        .collect(Collectors.toList());

        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                                "from OrderItem oi " +
                                "join oi.item i " +
                                "where oi.order.id in :orderIds ",
                        OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        // OrderId와 Item List를 그룹으로 매칭시킴
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto -> OrderItemQueryDto.getOrderId()));

        //Order 마다 Map의 Item을 추가시킴
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;

    }


    public List<OrderFlatDto> findAllByDtoFlat() {
        return em.createQuery(
                "select new jpabook.jpashop.dto.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count) " +
                        "from Order o " +
                        "join o.member m " +
                        "join o.delivery d " +
                        "join o.orderItems oi " +
                        "join oi.item i", OrderFlatDto.class
        ).getResultList();
    }

    private List<OrderQueryDto> findOrders(){
        return em.createQuery(
                "select new jpabook.jpashop.dto.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o" +
                        " join o.member m" +
                        " join o.delivery d",
                OrderQueryDto.class
        ).getResultList();
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                        "from OrderItem oi " +
                        "join oi.item i " +
                        "where oi.order.id = :orderId ",
                OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }



}
