package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order
 * Order -> Member
 * Order -> Delivery
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        //1. 무한루프에 빠짐. Order -> Member 조회 -> Order 조회...
        //-> 양방향 중 한곳은 JsonIgnore를 통해 끊어주어야 함
        //2. Member은 지연로딩으로 가져옴, 따라서 지연로딩으로 인해 값이 없는 것은 Hibernate가 대신 Proxy 객체 형태로 넣어둠.
        //   멤버객체에 손댈 때에야 Member를 가져옴(지연로딩)
        //-> 프록시 객체를 Json으로 바꿀 수 없어서 오류가 남 -> 뿌리지 말라고 명령을 해야 함
        //-> Hibernate5Module을 빈으로 등록하여 초기화 된 프록시 객체만 노출하도록 설정함
        // 결론 : 지연 로딩을 기본으로 하고, 성능 최적화가 필요한 경우에는 패치 조인을 사용!!
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        return orderRepository.findAllByString(new OrderSearch()).stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
    }

    //v1과 v2의 문제점 => N+1 문제, 처음 쿼리의 결과로 N번만큼 추가 실행이 됨
    //Order 조회시 N개의 데이터를 불러옴 + N번의 Member 지연로딩 쿼리 + N번의 Delivery 지연로딩 쿼리
    //최악의 경우 : Order이 2개면 1+2+2번 실행
    //만약 N개의 데이터의 Member가 같다면 1+1+2번 실행(이미 조회한 데이터는 영속성 컨텍스트에서 가져옴)



    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        //fetch join : LAZY 로딩하여 조인해서 가져옴
        List<Order> orders =  orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }


    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //LAZY 초기화
        }

    }
}
