package Project.Order.Management.System.controllerTest;

import Project.Order.Management.System.util.ProductFactory;
import Project.Order.Management.config.SecurityConfig;
import Project.Order.Management.controller.ProductController;
import Project.Order.Management.dto.request.CreateProductRequest;
import Project.Order.Management.dto.request.UpdateProductRequest;
import Project.Order.Management.dto.response.ProductResponse;
import Project.Order.Management.security.CustomUserDetailsService;
import Project.Order.Management.security.JwtService;
import Project.Order.Management.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

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
    ProductService productService;

    @Test
    @DisplayName("Should return 201 when product is created")
    void create_ShouldReturn201() throws Exception {

        CreateProductRequest request = ProductFactory.createProductRequest();
        ProductResponse response = ProductFactory.createProductResponse();

        given(productService.createProduct(request)).willReturn(response);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        then(productService).should().createProduct(request);
    }

    @Test
    @DisplayName("Should return 200 when product exists")
    void findById_ShouldReturn200() throws Exception {

        ProductResponse response = ProductFactory.createProductResponse();

        given(productService.findById(response.getId())).willReturn(response);

        mockMvc.perform(get("/products/{id}", response.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        then(productService).should().findById(response.getId());
    }

    @Test
    @DisplayName("Should return 200 when listing products")
    void listAll_ShouldReturn200() throws Exception {

        ProductResponse response = ProductFactory.createProductResponse();

        given(productService.findAll()).willReturn(List.of(response));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(List.of(response))
                ));

        then(productService).should().findAll();
    }

    @Test
    @DisplayName("Should return 204 when product is deleted")
    void delete_ShouldReturn204() throws Exception {

        ProductResponse response = ProductFactory.createProductResponse();

        mockMvc.perform(delete("/products/{id}", response.getId()))
                .andExpect(status().isNoContent());

        then(productService).should().delete(response.getId());
    }
}

