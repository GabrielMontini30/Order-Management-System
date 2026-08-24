package Project.Order.Management.domain;

import Project.Order.Management.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Double totalAmount;

    @ManyToOne(optional = false)
    @NotNull(message = "The customer must exist.")
    private Customer customer;

    @ManyToOne(optional = false)
    @NotNull (message = "The user must exist.")
    private Users user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItem= new ArrayList<>();

    public Order(Customer customer, Users user, List<OrderItem> items) {
        this.status = OrderStatus.CREATE;
        this.createdAt = new Date(System.currentTimeMillis());
        this.customer = customer;
        this.user = user;
        setOrderItem(items);
        calcularTotal();
    }

    public Order() {
        this.status = OrderStatus.CREATE;
        this.createdAt = new Date(System.currentTimeMillis());
    }

    public void setOrderItem(List<OrderItem> items) {
        if(items==null || items.isEmpty()){
            throw new IllegalArgumentException("The items not empty");
        }

        if(!podeAlterarItens()){
            throw new IllegalStateException("The status not be illegal");
        }
        orderItem.clear();

        for(OrderItem orderItem1:items){
            orderItem1.setOrder(this);
            this.orderItem.add(orderItem1);
        }

        calcularTotal();

    }

    public void atualizarStatus(OrderStatus novoStatus){
        validarTransicaoStatus(novoStatus);status=novoStatus;
    }

    private void validarTransicaoStatus(OrderStatus orderStatus){
        if(status==OrderStatus.CANCELED || status==OrderStatus.SENT) throw new IllegalStateException("Pedido não pode ser mais alterado");
        switch (status){
            case CREATE -> {
                if(!(orderStatus==OrderStatus.PAID || orderStatus== OrderStatus.CANCELED)){
                    throw new IllegalArgumentException("Transação invalida");
                }
            }
            case PAID -> {
                if (orderStatus != OrderStatus.SENT) {
                    throw new IllegalArgumentException("Transação invalida");
                }
            }
            default -> {
                throw new IllegalStateException("Estado inválido");
            }
        }
    }

    public boolean podeAlterarItens(){
        return status == OrderStatus.CREATE;
    }

    public boolean estaCancelado() {
        return status == OrderStatus.CANCELED;
    }

    public boolean estaFinalizado() {
        return status == OrderStatus.SENT;
    }

    public void calcularTotal(){
        this.totalAmount = orderItem.stream().mapToDouble(value -> value.getPriceAtMoment() * value.getQuantity()).sum();
    }

    public Long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Users getUser() {
        return user;
    }

    public List<OrderItem> getOrderItem() {
        return orderItem;
    }
}
