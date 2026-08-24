package Project.Order.Management.repository;

import Project.Order.Management.domain.Customer;
import Project.Order.Management.domain.Order;
import Project.Order.Management.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByCustomerAndStatus(Customer customer, OrderStatus status);
}
