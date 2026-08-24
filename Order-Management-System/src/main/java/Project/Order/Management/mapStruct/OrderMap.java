package Project.Order.Management.mapStruct;


import Project.Order.Management.domain.Order;
import Project.Order.Management.domain.OrderItem;
import Project.Order.Management.dto.response.OrderItemResponse;
import Project.Order.Management.dto.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper (componentModel = "spring", uses = {ProductMap.class, CustomerMap.class})
public interface OrderMap {

    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "orderItem", source = "orderItem")
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "product", source = "product")
    @Mapping(target = "priceAtMoment", source = "priceAtMoment")
    List<OrderResponse> toOrderResponseList(List<Order> orders);


}
