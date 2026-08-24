package Project.Order.Management.System.util;

import Project.Order.Management.domain.Product;
import Project.Order.Management.dto.request.CreateProductRequest;
import Project.Order.Management.dto.request.UpdateProductRequest;
import Project.Order.Management.dto.response.ProductResponse;

public class ProductFactory {

    public static Product createValidProduct() {
        return Product.builder()
                .id(1L)
                .name("Notebook")
                .description("Dell")
                .price(3000.0)
                .quantity(10.0)
                .build();
    }

    public static CreateProductRequest createProductRequest() {
        return CreateProductRequest.builder()
                .name("Notebook")
                .description("Dell")
                .price(3000.0)
                .quantity(10.0)
                .build();
    }

    public static ProductResponse createProductResponse() {
        return ProductResponse.builder()
                .id(1L)
                .name("Notebook")
                .price(3000.0)
                .quantity(10.0)
                .build();
    }

    public static UpdateProductRequest createUpdateProductRequest() {
        return UpdateProductRequest.builder()
                .name("Notebook")
                .description("Dell")
                .price(3000.0)
                .quantity(10.0)
                .build();
    }
}

