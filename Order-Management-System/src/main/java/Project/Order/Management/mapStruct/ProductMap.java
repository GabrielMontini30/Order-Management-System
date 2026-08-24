package Project.Order.Management.mapStruct;

import Project.Order.Management.domain.Product;
import Project.Order.Management.dto.request.CreateProductRequest;
import Project.Order.Management.dto.request.UpdateProductRequest;
import Project.Order.Management.dto.response.ProductResponse;
import Project.Order.Management.service.ProductService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMap {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Product toProduct(CreateProductRequest productRequest);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "quantity", source = "quantity")

    ProductResponse toProductResponse(Product product);

    List<ProductResponse> toListProduct(List<Product> productList);
}
