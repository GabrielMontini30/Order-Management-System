package Project.Order.Management.service;

import Project.Order.Management.domain.*;
import Project.Order.Management.dto.request.CreateOrderRequest;
import Project.Order.Management.dto.request.UpdateOrderStatusRequest;
import Project.Order.Management.dto.response.OrderResponse;
import Project.Order.Management.exception.ResourceNotFoundException;
import Project.Order.Management.mapStruct.OrderMap;
import Project.Order.Management.repository.CustomerRepository;
import Project.Order.Management.repository.OrderRepository;
import Project.Order.Management.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMap orderMap;
    private final ProductService productService;

    @Transactional
    public OrderResponse create(CreateOrderRequest orderRequest, Users user){

        if(orderRequest==null || orderRequest.getItemsList().isEmpty()) throw new IllegalArgumentException("The item List not be empty");

        Customer customerFound = customerRepository.findById(orderRequest.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("customer not found"));

        List<OrderItem> items= new ArrayList<>();

        Order order= new Order();

        order.setCustomer(customerFound);
        order.setUser(user);

        for(CreateOrderRequest.ItemRequest requestItems: orderRequest.getItemsList()){

                if(requestItems.getQuantity()<0){
                    throw new IllegalArgumentException("Item quantity must be more than zero");
                }

            Product productFound = productRepository.findById(requestItems.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));


            OrderItem item= new OrderItem();
            item.setOrder(order);
            item.setProduct(productFound);
            item.setQuantity(requestItems.getQuantity());
            item.setPriceAtMoment(productFound.getPrice());
            items.add(item);
        }

        productService.verifyStock(items);

        for(OrderItem item : items){
            Product p = item.getProduct();
            p.setQuantity(p.getQuantity() - item.getQuantity());
            productRepository.save(p);
        }

        order.setOrderItem(items);
        Order orderSaved=repository.save(order);

        return orderMap.toOrderResponse(orderSaved);
    }

    public OrderResponse findById(Long id){

        Order orderFounded = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("The Order not found"));

        return orderMap.toOrderResponse(orderFounded);
    }

    public List<OrderResponse> listAllOrders() {
        List<Order> allOrders = repository.findAll();
        return orderMap.toOrderResponseList(allOrders);
    }

    public List<OrderResponse> listByCustomer(Long customerId){

        Customer customerFound = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("The Order not found"));

        List<Order> orderByCustomer = repository.findByCustomer(customerFound);

        return orderMap.toOrderResponseList(orderByCustomer);

    }

    public OrderResponse updateStatus(UpdateOrderStatusRequest orderStatusRequest, Long id){
        if (orderStatusRequest == null || orderStatusRequest.getStatus() == null) {
            throw new IllegalArgumentException("The status must not be null");
        }

        Order orderFounded = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("The Order not found"));

        orderFounded.atualizarStatus(orderStatusRequest.getStatus());

        repository.save(orderFounded);

        return orderMap.toOrderResponse(orderFounded);


    }



}
