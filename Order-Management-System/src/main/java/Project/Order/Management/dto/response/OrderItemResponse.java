package Project.Order.Management.dto.response;

import Project.Order.Management.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class OrderItemResponse {
    private ProductResponse product;
    private Double quantity;
    private Double priceAtMoment;

}
