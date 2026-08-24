 package Project.Order.Management.System.controllerTest;

import Project.Order.Management.System.util.AuthFactory;
import Project.Order.Management.System.util.OrderFactory;
import Project.Order.Management.config.SecurityConfig;
import Project.Order.Management.controller.OrderController;
import Project.Order.Management.domain.Order;
import Project.Order.Management.domain.Users;
import Project.Order.Management.dto.request.CreateOrderRequest;
import Project.Order.Management.dto.request.UpdateOrderStatusRequest;
import Project.Order.Management.dto.response.OrderResponse;
import Project.Order.Management.enums.OrderStatus;
import Project.Order.Management.exception.ResourceNotFoundException;
import Project.Order.Management.security.CustomUserDetailsService;
import Project.Order.Management.security.JwtService;
import Project.Order.Management.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

 @WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SecurityConfig securityConfig;

    @MockBean
    JwtService jwtService;

    @MockBean
    CustomUserDetailsService userDetailsServices;


    @MockBean
    OrderService orderService;

    @Test
    @DisplayName("Should return 201 when order is created")
    void createOrder_ShouldReturn201() throws Exception {

        CreateOrderRequest request = OrderFactory.createOrderRequest();

        OrderResponse response = OrderFactory.createOrderResponse();


        Users user = AuthFactory.createValidAuth();

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));


        given(orderService.create(any(CreateOrderRequest.class), any(Users.class))).willReturn(response);


        mockMvc.perform(post("/orders") .with(authentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        then(orderService).should().create(any(CreateOrderRequest.class), any(Users.class));
    }

     @Test
     @DisplayName("Should return 200 when order is found by id")
     void findById_ShouldReturn200() throws Exception {
         OrderResponse orderResponse = OrderFactory.createOrderResponse();

         given(orderService.findById(orderResponse.getId())).willReturn(orderResponse);

         mockMvc.perform(get("/orders/{id}", orderResponse.getId()))
                 .andExpect(status().isOk())
                 .andExpect(content().json(objectMapper.writeValueAsString(orderResponse)));

         then(orderService).should().findById(orderResponse.getId());
     }

     @Test
     @DisplayName("Should return 404 when order does not exist")
     void findById_WhenOrderDoesNotExist_ShouldReturn404() throws Exception {
         given(orderService.findById(anyLong())).willThrow(new ResourceNotFoundException("The Order not found"));

         mockMvc.perform(get("/orders/{id}", 999L))
                 .andExpect(status().isNotFound());

         then(orderService).should().findById(999L);
     }

     @Test
     @DisplayName("Should return 200 and the full order list")
     void listAll_ShouldReturn200() throws Exception {
         OrderResponse orderResponse = OrderFactory.createOrderResponse();

         given(orderService.listAllOrders()).willReturn(List.of(orderResponse));

         mockMvc.perform(get("/orders"))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.length()").value(1));

         then(orderService).should().listAllOrders();
     }


     @Test
     @DisplayName("Should return 200 when update status of order")
     void updateStatus_ShouldReturn200() throws Exception {

             OrderResponse orderResponse = OrderFactory.createOrderResponsePaid();

             UpdateOrderStatusRequest statusRequest = UpdateOrderStatusRequest.builder().status(OrderStatus.PAID).build();

             given(orderService.updateStatus(statusRequest, 1L)).willReturn(orderResponse);

             mockMvc.perform(patch("/orders/{orderId}", 1L)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(objectMapper.writeValueAsString(statusRequest)))
                     .andExpect(status().isOk())
                     .andExpect(content().json(objectMapper.writeValueAsString(orderResponse)));

             then(orderService).should().updateStatus(statusRequest, 1L);
         }
     }


