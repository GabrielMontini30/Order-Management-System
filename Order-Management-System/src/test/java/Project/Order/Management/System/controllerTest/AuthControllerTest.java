package Project.Order.Management.System.controllerTest;

import Project.Order.Management.System.util.AuthFactory;
import Project.Order.Management.config.SecurityConfig;
import Project.Order.Management.controller.AuthController;
import Project.Order.Management.dto.request.LoginRequest;
import Project.Order.Management.dto.request.RegisterRequest;
import Project.Order.Management.dto.response.LoginResponse;
import Project.Order.Management.dto.response.RegisterResponse;
import Project.Order.Management.security.CustomUserDetailsService;
import Project.Order.Management.security.JwtService;
import Project.Order.Management.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

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
    AuthService authService;

    @Test
    @DisplayName("Should return 200 when login is successful")
    void login_ShouldReturn200() throws Exception {

        LoginRequest request = AuthFactory.createLoginRequest();
        LoginResponse response = AuthFactory.createLoginResponse();

        given(authService.login(request)).willReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        then(authService).should().login(request);
    }

    @Test
    @DisplayName("Should return 201 when register is successful")
    void register_ShouldReturn201() throws Exception {

        RegisterRequest request = AuthFactory.createRegisterRequest();
        RegisterResponse response = AuthFactory.createRegisterResponse();

        given(authService.register(request)).willReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        then(authService).should().register(request);
    }
}
