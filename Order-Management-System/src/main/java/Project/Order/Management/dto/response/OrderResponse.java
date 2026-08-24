package Project.Order.Management.dto.response;

import Project.Order.Management.domain.Customer;
import Project.Order.Management.domain.OrderItem;
import Project.Order.Management.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private Date createdAt;
    private OrderStatus status;
    private Double totalAmount;
    private CustomerResponse customer;
    private List<OrderItemResponse> orderItem;
}
