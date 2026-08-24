package Project.Order.Management.System.util;

import Project.Order.Management.domain.*;
import Project.Order.Management.dto.request.CreateOrderRequest;
import Project.Order.Management.dto.response.CustomerResponse;
import Project.Order.Management.dto.response.OrderItemResponse;
import Project.Order.Management.dto.response.OrderResponse;
import Project.Order.Management.dto.response.ProductResponse;
import Project.Order.Management.enums.OrderStatus;
import Project.Order.Management.enums.UserRoles;

import java.util.Date;
import java.util.List;

public class OrderFactory {

    private final static Product product =ProductFactory.createValidProduct();
    private final static ProductResponse productResponse =ProductFactory.createProductResponse();
    private final static Users user = AuthFactory.createValidAuth();
    private final static Customer customer = CustomerFactory.createValidCustomer();
    private final static CustomerResponse customerResponse = CustomerFactory.createCustomerResponse();

    public static Order createValidOrder() {

        OrderItem item = new OrderItem();

        item.setProduct(product);
        item.setQuantity(2.0);
        item.setPriceAtMoment(product.getPrice());

        return new Order(customer, user, List.of(item));
    }


    public static CreateOrderRequest createOrderRequest(){

        CreateOrderRequest.ItemRequest item =
                CreateOrderRequest.ItemRequest.builder()
                        .productId(1L)
                        .quantity(2.0)
                        .build();

        return CreateOrderRequest.builder()
                .customerId(1L)
                .itemsList(List.of(item))
                .build();
    }

    public static OrderResponse createOrderResponse(){


        OrderItemResponse itemResponse = OrderItemResponse.builder()
                .product(productResponse)
                .quantity(2.0)
                .priceAtMoment(3000.0)
                .build();

        return OrderResponse.builder()
                .id(1L)
                .createdAt(new Date())
                .status(OrderStatus.CREATE)
                .totalAmount(6000.0)
                .customer(customerResponse)
                .orderItem(List.of(itemResponse))
                .build();
    }

    public static OrderResponse createOrderResponsePaid(){


        OrderItemResponse itemResponse = OrderItemResponse.builder()
                .product(productResponse)
                .quantity(2.0)
                .priceAtMoment(3000.0)
                .build();

        return OrderResponse.builder()
                .id(1L)
                .createdAt(new Date())
                .status(OrderStatus.PAID)
                .totalAmount(6000.0)
                .customer(customerResponse)
                .orderItem(List.of(itemResponse))
                .build();
    }


}
