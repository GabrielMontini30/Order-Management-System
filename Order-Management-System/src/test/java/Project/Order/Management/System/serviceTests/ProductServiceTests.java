package Project.Order.Management.System.serviceTests;

import Project.Order.Management.System.util.ProductFactory;
import Project.Order.Management.domain.OrderItem;
import Project.Order.Management.domain.Product;
import Project.Order.Management.dto.request.CreateProductRequest;
import Project.Order.Management.dto.request.UpdateProductRequest;
import Project.Order.Management.dto.response.ProductResponse;
import Project.Order.Management.exception.ResourceNotFoundException;
import Project.Order.Management.exception.StockException;
import Project.Order.Management.mapStruct.ProductMap;
import Project.Order.Management.repository.ProductRepository;
import Project.Order.Management.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMap productMap;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);

        productService = new ProductService(productRepository, productMap);
    }

    @Test
    @DisplayName("When try create product return successfully ")
    void createProduct_shouldCreateProductSuccessfully(){
        Product product = ProductFactory.createValidProduct();
        CreateProductRequest productRequest = ProductFactory.createProductRequest();
        ProductResponse productResponse = ProductFactory.createProductResponse();

        BDDMockito.given(productMap.toProduct(productRequest)).willReturn(product);
        BDDMockito.given(productMap.toProductResponse(product)).willReturn(productResponse);

        ProductResponse results = productService.createProduct(productRequest);

        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results.getId()).isEqualTo(productResponse.getId());
        Assertions.assertThat(results.getName()).isEqualTo(productResponse.getName());
        Assertions.assertThat(results.getPrice()).isEqualTo(productResponse.getPrice());

        BDDMockito.then(productMap).should().toProduct(productRequest);
        BDDMockito.then(productRepository).should().save(product);
        BDDMockito.then(productMap).should().toProductResponse(product);
    }

    @Test
    @DisplayName("when try update product return successfully")
    void updateProduct_shouldUpdateProductSuccessfully(){
        Product product = ProductFactory.createValidProduct();
        UpdateProductRequest updateRequest = ProductFactory.createUpdateProductRequest();
        ProductResponse productResponse = ProductFactory.createProductResponse();

        BDDMockito.given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        BDDMockito.given(productMap.toProductResponse(product)).willReturn(productResponse);

        ProductResponse results = productService.updateProduct(product.getId(), updateRequest);

        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results.getId()).isEqualTo(productResponse.getId());
        Assertions.assertThat(results.getName()).isEqualTo(productResponse.getName());
        Assertions.assertThat(results.getPrice()).isEqualTo(productResponse.getPrice());

        BDDMockito.then(productRepository).should().findById(product.getId());
        BDDMockito.then(productRepository).should().save(product);
        BDDMockito.then(productMap).should().toProductResponse(product);
    }

    @Test
    @DisplayName("when product not found throw exception")
    void updateProduct_shouldThrowExceptionWhenProductNotFound(){
        UpdateProductRequest updateRequest = ProductFactory.createUpdateProductRequest();

        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> productService.updateProduct(1L, updateRequest));

        then(productRepository).should().findById(1L);
        then(productRepository).should(BDDMockito.never()).save(any());
    }

    @Test
    @DisplayName("when try to validate stock return successfully")
    void verifyStock_shouldValidateStockSuccessfully(){
        Product product = ProductFactory.createValidProduct();
        OrderItem item = new OrderItem(1L, null, product, 5.0);

        Assertions.assertThatCode(()-> productService.verifyStock(List.of(item))).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("when stock is insufficient throw exception")
    void verifyStock_shouldThrowStockExceptionWhenStockIsInsufficient(){
        Product product = ProductFactory.createValidProduct();
        OrderItem item = new OrderItem(1L, null, product, 20.0);

        Assertions.assertThatExceptionOfType(StockException.class).isThrownBy(() -> productService.verifyStock(List.of(item)));
    }
}