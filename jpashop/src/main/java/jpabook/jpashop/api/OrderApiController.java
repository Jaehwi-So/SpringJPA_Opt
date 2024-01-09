package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.dto.OrderFlatDto;
import jpabook.jpashop.dto.OrderItemQueryDto;
import jpabook.jpashop.dto.OrderQueryDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * Order
 * Order -> Member
 * Order -> Delivery
 */

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }




    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3(){
        List<Order> orders =  orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
    }


    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_Paging(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ){
        List<Order> orders =  orderRepository.findAllWithItem(offset, limit);

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return result;
    }



    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> OrdersV4(){
        List<OrderQueryDto> result =  orderQueryRepository.findOrderQueryDtos();

        return result;
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> OrdersV5(){
        List<OrderQueryDto> result =  orderQueryRepository.findAllByDtoOptimization();

        return result;
    }


    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> OrdersV6(){
        List<OrderFlatDto> flats =  orderQueryRepository.findAllByDtoFlat();

        //애플리케이션 메모리단에서 중복을 제거
        Map<Long, List<OrderItemQueryDto>> orderItemMap = new HashMap<>();
        Map<Long, OrderQueryDto> orderMap = new HashMap<>();

        flats.forEach(flat -> {
            Long orderId = flat.getOrderId();
            if(orderMap.get(orderId) == null){
                orderMap.put(orderId,new OrderQueryDto(orderId, flat.getName(), flat.getOrderDate(), flat.getOrderStatus(), flat.getAddress()));
            }

            if(orderItemMap.get(orderId) == null){
                orderItemMap.put(orderId, new ArrayList<OrderItemQueryDto>());
            }
            orderItemMap.get(orderId).add(new OrderItemQueryDto(orderId, flat.getItemName(), flat.getOrderPrice(), flat.getCount()));
        });

        orderItemMap.forEach((orderId, orderItem)->{
            orderMap.get(orderId).setOrderItems(orderItem);
        });

        List<OrderQueryDto> result = new ArrayList<>(orderMap.values());
        return result;

    }



//    @Data
//    static class OrderDto{
//        private Long orderId;
//        private String name;
//        private LocalDateTime orderDate;
//        private OrderStatus orderStatus;
//        private Address address;
//        private List<OrderItem> orderItems;
//
//        public OrderDto(Order order){
//            orderId = order.getId();
//            name = order.getMember().getName();
//            orderDate = order.getOrderDate();
//            orderStatus = order.getStatus();
//            address = order.getDelivery().getAddress();
//            orderItems = order.getOrderItems();
//        }
//    }


    @Data
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(o -> new OrderItemDto(o))
                    .collect(Collectors.toList());

        }
    }

    @Data
    static class OrderItemDto{
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem){
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }


}
