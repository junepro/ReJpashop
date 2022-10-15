package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {

    public final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();

        result.forEach(o->{
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id,i.name,oi.orderPrice,oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id,m.name,o.orderDate,o.orderStatus,d.address)"+
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();

        List<Long> orderIds = result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());


        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id,i.name,oi.orderPrice,oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        //기존보다 맵핑된 값 최소한으로 가져옴 최적화 메모리에 올려서 실행
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;

    }
    public List<OrderFlatDto> findAllByDto_flat() {

        return em.createQuery(
                        "select new " +
                                " jpabook.jpashop.repository.order.query.OrderFlatDto(o.id,m.name,o.orderDate,o.orderStatus,d.address,i.name,oi.orderPrice,oi.count)"+
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d" +
                                " join o.orderItems oi" +
                                " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}
