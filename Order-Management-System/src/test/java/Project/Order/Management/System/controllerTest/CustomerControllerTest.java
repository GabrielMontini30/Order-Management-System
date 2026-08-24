 package Project.Order.Management.System.controllerTest;

import Project.Order.Management.System.util.CustomerFactory;
import Project.Order.Management.System.util.ProductFactory;
import Project.Order.Management.config.SecurityConfig;
import Project.Order.Management.controller.CustomerController;
import Project.Order.Management.dto.request.CreateCustomerRequest;
import Project.Order.Management.dto.response.CustomerResponse;
import Project.Order.Management.dto.response.ProductResponse;
import Project.Order.Management.security.CustomUserDetailsService;
import Project.Order.Management.security.JwtService;
import Project.Order.Management.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CustomerControllerTest {

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
    CustomerService customerService;

    @Test
    @DisplayName("Should return 201 when customer is created")
    void create_ShouldReturn201() throws Exception {

        CreateCustomerRequest request = CustomerFactory.createValidCustomerRequest();

        CustomerResponse response = CustomerFactory.createCustomerResponse();

        given(customerService.createCustomer(request, "gabriel@gmail.com")).willReturn(response);

        Principal principal = () -> "gabriel@gmail.com";

        mockMvc.perform(post("/customers")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        then(customerService).should().createCustomer(request, "gabriel@gmail.com");
    }

    @Test
    @DisplayName("Should return 200 when customer exists")
    void findById_ShouldReturn200() throws Exception {

        CustomerResponse response = CustomerFactory.createCustomerResponse();

        given(customerService.findById(response.getId())).willReturn(response);

        mockMvc.perform(get("/customers/{id}", response.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        then(customerService).should().findById(response.getId());
    }

    @Test
    @DisplayName("Should return 200 when listing customers")
    void listAll_ShouldReturn200() throws Exception {

        CustomerResponse response = CustomerFactory.createCustomerResponse();

        given(customerService.listAll()).willReturn(java.util.List.of(response));

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(java.util.List.of(response))));

        then(customerService).should().listAll();
    }

    @Test
    @DisplayName("Should return 204 when customer is deleted")
    void delete_ShouldReturn204() throws Exception {

        CustomerResponse response = CustomerFactory.createCustomerResponse();

        mockMvc.perform(delete("/customers/{id}", response.getId()))
                .andExpect(status().isNoContent());

        then(customerService).should().delete(response.getId());
    }
}
