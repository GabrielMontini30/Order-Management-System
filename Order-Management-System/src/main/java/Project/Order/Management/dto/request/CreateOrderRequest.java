package Project.Order.Management.dto.request;

import Project.Order.Management.domain.OrderItem;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    @NotNull
    private Long customerId;

    @NotNull
    private List<ItemRequest> itemsList;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemRequest {
        @NotNull
        private Long productId;

        @NotNull
        @Positive
        private Double quantity;

    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<ItemRequest> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<ItemRequest> itemsList) {
        this.itemsList = itemsList;
    }
}
