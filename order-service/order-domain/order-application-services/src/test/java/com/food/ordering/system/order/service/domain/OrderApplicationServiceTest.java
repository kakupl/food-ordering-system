package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.create.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.input.services.OrderApplicationService;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;
    private final UUID CUSTOMER_ID = UUID.fromString("8f635706-2954-414b-b961-c0554cc22c9c");
    private final UUID RESTAURANT_ID = UUID.fromString("ec00817c-1527-48b5-990d-5f65b12a7d51");
    private final UUID PRODUCT_ID = UUID.fromString("ac967a90-8514-4336-ac0e-df0b622153bf");
    private final UUID ORDER_ID = UUID.fromString("3f0b6424-b2c3-4002-b146-41415bf35912");
    private final BigDecimal PRICE = new BigDecimal("200.00");

    @BeforeAll
    public void init(){
        createOrderCommand = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street_1")
                        .postalCode("62-035")
                        .city("Kórnik")
                        .build())
                .price(PRICE)
                .items(List.of(OrderItem.builder()
                        .productId(PRODUCT_ID)
                        .quantity(1)
                        .price(new BigDecimal("50.00"))
                        .subTotal(new BigDecimal("50.00"))
                        .build(),
                OrderItem.builder()
                        .productId(PRODUCT_ID)
                        .quantity(3)
                        .price(new BigDecimal("50.00"))
                        .subTotal(new BigDecimal("150.00"))
                        .build()))
                .build();


//        createOrderCommandWrongPrice = CreateOrderCommand.builder()
//                .customerId(CUSTOMER_ID)
//                .restaurantId(RESTAURANT_ID)
//                .address(OrderAddress.builder()
//                        .street("street_1")
//                        .postalCode("62-035")
//                        .city("Kórnik")
//                        .build())
//                .price(new BigDecimal("250.00"))
//                .items(List.of(OrderItem.builder()
//                                .productId(PRODUCT_ID)
//                                .quantity(1)
//                                .price(new BigDecimal("50.00"))
//                                .subTotal(new BigDecimal("50.00"))
//                                .build(),
//                        OrderItem.builder()
//                                .productId(PRODUCT_ID)
//                                .quantity(3)
//                                .price(new BigDecimal("50.00"))
//                                .subTotal(new BigDecimal("150.00"))
//                                .build()))
//                .build();
//
//
//        createOrderCommandWrongProductPrice = CreateOrderCommand.builder()
//                .customerId(CUSTOMER_ID)
//                .restaurantId(RESTAURANT_ID)
//                .address(OrderAddress.builder()
//                        .street("street_1")
//                        .postalCode("62-035")
//                        .city("Kórnik")
//                        .build())
//                .price(new BigDecimal("210.00"))
//                .items(List.of(OrderItem.builder()
//                                .productId(PRODUCT_ID)
//                                .quantity(1)
//                                .price(new BigDecimal("60.00"))
//                                .subTotal(new BigDecimal("60.00"))
//                                .build(),
//                        OrderItem.builder()
//                                .productId(PRODUCT_ID)
//                                .quantity(3)
//                                .price(new BigDecimal("50.00"))
//                                .subTotal(new BigDecimal("150.00"))
//                                .build()))
//                .build();

        Customer customer = new Customer();
        customer.setId(new CustomerId(CUSTOMER_ID));

        Restaurant restaurantResponse = Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(List.of(
                        new Product(
                                new ProductId(PRODUCT_ID),"product-1",
                                new Money(
                                new BigDecimal("50.00"))),
                        new Product(
                                new ProductId(PRODUCT_ID),"product-2",
                                new Money(
                                new BigDecimal("50.00")))
                ))
                .active(true)
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));

        when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));

        when(orderRepository.save(any(Order.class))).thenReturn(order);

    }

    @Test
    public void testCreateOrder(){

        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        assertEquals(createOrderResponse.getOrderStatus(), OrderStatus.PENDING);
        assertEquals(createOrderResponse.getMessage(),"Order created successfully");
        assertNotNull(createOrderResponse.getOrderTrackingId());
    }

}
