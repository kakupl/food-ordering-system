package com.food.ordering.system.order.service.dataacces.order.adapter;

import com.food.ordering.system.order.service.dataacces.order.mapper.OrderDataAccessMapper;
import com.food.ordering.system.order.service.dataacces.order.repository.OrderJpaRepository;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

   public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository,
                              OrderDataAccessMapper orderDataAccessMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderDataAccessMapper = orderDataAccessMapper;
    }

    @Override
    public Order save(final Order order) {
        return orderDataAccessMapper.orderEntityToOrder(
                orderJpaRepository.save(
                        orderDataAccessMapper.orderToOrderEntity(order))
        );
    }

    @Override
    public Optional<Order> findByTrackingId(final TrackingId trackingId) {
        return orderJpaRepository.findByTrackingId(trackingId.getValue())
                .map(orderDataAccessMapper::orderEntityToOrder);
    }
}
