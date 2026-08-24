package Project.Order.Management.controller;


import Project.Order.Management.domain.Users;
import Project.Order.Management.dto.request.CreateOrderRequest;
import Project.Order.Management.dto.request.UpdateOrderStatusRequest;
import Project.Order.Management.dto.response.OrderResponse;
import Project.Order.Management.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Endpoints to managing orders.")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order", description = "Method for create a new order")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created with successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "401", description = "Customer isn't authenticated"),
            @ApiResponse(responseCode = "404", description = "Customer ou product not founded"),
            @ApiResponse(responseCode = "409", description = "Insufficient stock")
    })
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request,
                                                @AuthenticationPrincipal Users users)
    {
        return new ResponseEntity<>(orderService.create(request, users), HttpStatus.CREATED);
    }


    @GetMapping
    @Operation(summary = "List all order", description = "Method for list all orders")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of orders returned successfully"),
            @ApiResponse(responseCode = "401", description = "Customer isn't authenticated")
            })
    public ResponseEntity<List<OrderResponse>> listAllOrders()
    {
        return new ResponseEntity<>(orderService.listAllOrders(), HttpStatus.OK);
    }

    @GetMapping ("/{id}")
    @Operation(summary = "Find order by id", description = "Method for find orders by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order returned successfully"),
            @ApiResponse(responseCode = "401", description = "Customer isn't authenticated"),
            @ApiResponse(responseCode = "404", description = "Product not founded")

    })
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id)
    {
        return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
    }


    @GetMapping ("/customers/{customerId}")
    @Operation(summary = "find an order by a specific customer", description = "Method for find an order by a specific customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order returned successfully"),
            @ApiResponse(responseCode = "401", description = "Customer isn't authenticated"),
            @ApiResponse(responseCode = "404", description = "Customer not founded")
    })
    public ResponseEntity<List<OrderResponse>> listByCustomer(@PathVariable Long customerId)
    {
        return new ResponseEntity<>(orderService.listByCustomer(customerId),HttpStatus.OK);
    }

    @PatchMapping ("/{orderId}")
    @Operation(summary = "Update status order", description = "Method for update status order")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Updated status successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "401", description = "Customer isn't authenticated"),
            @ApiResponse(responseCode = "404", description = "Product not founded"),
            @ApiResponse(responseCode = "409", description = "Status transition not allowed ")
    })
    public ResponseEntity<OrderResponse> updateStatus(@RequestBody  UpdateOrderStatusRequest orderStatusRequest, @PathVariable Long orderId)
    {
        return new ResponseEntity<>(orderService.updateStatus(orderStatusRequest,orderId),HttpStatus.OK);
    }



}