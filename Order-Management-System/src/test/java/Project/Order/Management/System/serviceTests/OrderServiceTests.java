package Project.Order.Management.System.serviceTests;

import Project.Order.Management.System.util.AuthFactory;
import Project.Order.Management.System.util.CustomerFactory;
import Project.Order.Management.System.util.OrderFactory;
import Project.Order.Management.System.util.ProductFactory;
import Project.Order.Management.domain.*;
import Project.Order.Management.dto.request.CreateOrderRequest;
import Project.Order.Management.dto.request.UpdateOrderStatusRequest;
import Project.Order.Management.dto.response.OrderResponse;
import Project.Order.Management.enums.OrderStatus;
import Project.Order.Management.exception.ResourceNotFoundException;
import Project.Order.Management.exception.StockException;
import Project.Order.Management.mapStruct.OrderMap;
import Project.Order.Management.repository.CustomerRepository;
import Project.Order.Management.repository.OrderRepository;
import Project.Order.Management.repository.ProductRepository;
import Project.Order.Management.service.OrderService;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @Mock
    private OrderRepository repository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMap orderMap;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        orderService = new OrderService(
                repository,
                customerRepository,
                productRepository,
                orderMap,
                productService
        );
    }

    @Test
    @DisplayName("When try create order return successfully")
    void create_shouldCreateSuccessfully() {

        Order order = OrderFactory.createValidOrder();
        Users user = AuthFactory.createValidAuth();
        Customer customer = CustomerFactory.createValidCustomer();
        Product product = ProductFactory.createValidProduct();

        CreateOrderRequest orderRequest = OrderFactory.createOrderRequest();
        OrderResponse orderResponse = OrderFactory.createOrderResponse();

        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        given(productRepository.findById(orderRequest.getItemsList().getFirst().getProductId())).willReturn(Optional.of(product));

        given(repository.save(any(Order.class))).willReturn(order);

        given(orderMap.toOrderResponse(order)).willReturn(orderResponse);

        OrderResponse results = orderService.create(orderRequest, user);

        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results.getOrderItem()).hasSize(1);
        Assertions.assertThat(results.getOrderItem().getFirst().getQuantity()).isEqualTo(orderRequest.getItemsList().getFirst().getQuantity());

        then(customerRepository).should().findById(customer.getId());

        then(productRepository).should().findById(orderRequest.getItemsList().getFirst().getProductId());

        then(productService).should().verifyStock(any());

        then(productRepository).should().save(product);

        then(repository).should().save(any(Order.class));

        then(orderMap).should().toOrderResponse(order);
    }

    @Test
    @DisplayName("When item list is empty throw exception")
    void create_shouldThrowExceptionWhenItemListIsEmpty() {

        CreateOrderRequest orderRequest = OrderFactory.createOrderRequest();
        orderRequest.setItemsList(java.util.List.of());

        Users user = AuthFactory.createValidAuth();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> orderService.create(orderRequest, user));

        then(customerRepository).shouldHaveNoInteractions();
        then(productRepository).shouldHaveNoInteractions();
        then(repository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("When customer not found throw exception")
    void create_shouldThrowExceptionWhenCustomerNotFound() {

        CreateOrderRequest orderRequest = OrderFactory.createOrderRequest();
        Users user = AuthFactory.createValidAuth();

        given(customerRepository.findById(orderRequest.getCustomerId())).willReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> orderService.create(orderRequest, user));

        then(customerRepository).should().findById(orderRequest.getCustomerId());

        then(productRepository).shouldHaveNoInteractions();
        then(repository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("When product not found throw exception")
    void create_shouldThrowExceptionWhenProductNotFound() {

        CreateOrderRequest orderRequest = OrderFactory.createOrderRequest();
        Users user = AuthFactory.createValidAuth();
        Customer customer = CustomerFactory.createValidCustomer();

        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        given(productRepository.findById(orderRequest.getItemsList().getFirst().getProductId())).willReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> orderService.create(orderRequest, user));

        then(customerRepository).should().findById(customer.getId());

        then(productRepository).should().findById(orderRequest.getItemsList().getFirst().getProductId());

        then(productService).shouldHaveNoInteractions();

        then(repository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("When stock is insufficient throw StockException")
    void create_shouldThrowStockExceptionWhenStockIsInsufficient() {

        CreateOrderRequest orderRequest = OrderFactory.createOrderRequest();
        Users user = AuthFactory.createValidAuth();
        Customer customer = CustomerFactory.createValidCustomer();
        Product product = ProductFactory.createValidProduct();

        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        given(productRepository.findById(orderRequest.getItemsList().getFirst().getProductId())).willReturn(Optional.of(product));

        BDDMockito.doThrow(new StockException("Insufficient stock")).when(productService).verifyStock(any());

        Assertions.assertThatExceptionOfType(StockException.class).isThrownBy(() -> orderService.create(orderRequest, user));

        then(customerRepository).should().findById(customer.getId());

        then(productRepository).should().findById(orderRequest.getItemsList().getFirst().getProductId());

        then(productService).should().verifyStock(any());

        then(productRepository).shouldHaveNoMoreInteractions();
        then(repository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("When create order update product stock successfully")
    void create_shouldCalculateTotalAmountCorrectly() {

        Order order = OrderFactory.createValidOrder();
        Users user = AuthFactory.createValidAuth();
        Customer customer = CustomerFactory.createValidCustomer();
        Product product = ProductFactory.createValidProduct();

        CreateOrderRequest orderRequest = OrderFactory.createOrderRequest();
        OrderResponse orderResponse = OrderFactory.createOrderResponse();

        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        given(productRepository.findById(orderRequest.getItemsList().getFirst().getProductId())).willReturn(Optional.of(product));

        given(repository.save(any(Order.class))).willReturn(order);

        given(orderMap.toOrderResponse(order)).willReturn(orderResponse);

        Double stockBefore = product.getQuantity();

        orderService.create(orderRequest, user);

        Double quantityPurchased = orderRequest.getItemsList().getFirst().getQuantity();

        Assertions.assertThat(product.getQuantity()).isEqualTo(stockBefore - quantityPurchased);

        then(productRepository).should().save(product);

        then(productService).should().verifyStock(any());

        then(repository).should().save(any(Order.class));
    }

    @Test
    @DisplayName("When update order status return successfully")
    void updateStatus_shouldUpdateStatusSuccessfully() {

        Order order = OrderFactory.createValidOrder();

        OrderResponse orderResponse = OrderFactory.createOrderResponse();

        UpdateOrderStatusRequest statusRequest = UpdateOrderStatusRequest.builder().status(OrderStatus.PAID).build();

        given(repository.findById(order.getId())).willReturn(Optional.of(order));

        given(repository.save(order)).willReturn(order);

        given(orderMap.toOrderResponse(order)).willReturn(orderResponse);

        OrderResponse results = orderService.updateStatus(statusRequest, order.getId());

        Assertions.assertThat(results).isNotNull();

        then(repository).should().findById(order.getId());

        then(repository).should().save(order);

        then(orderMap).should().toOrderResponse(order);
    }
}
