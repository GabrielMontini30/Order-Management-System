package Project.Order.Management.System.serviceTests;

import Project.Order.Management.System.util.AuthFactory;
import Project.Order.Management.domain.Users;
import Project.Order.Management.dto.request.LoginRequest;
import Project.Order.Management.dto.request.RegisterRequest;
import Project.Order.Management.dto.response.LoginResponse;
import Project.Order.Management.dto.response.RegisterResponse;
import Project.Order.Management.exception.BusinessException;
import Project.Order.Management.mapStruct.UsersMap;
import Project.Order.Management.repository.UserRepository;
import Project.Order.Management.security.JwtService;
import Project.Order.Management.service.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(SpringExtension.class)
public class AuthServiceTest {
   @Mock
   private  UserRepository userRepository;

   @Mock
   private  UsersMap usersMap;

   @Mock
   private  AuthenticationManager manager;

   @Mock
   private PasswordEncoder encoder;

   @Mock
   private  JwtService jwtService;

   @InjectMocks
   AuthService authService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);

        authService= new AuthService(userRepository, usersMap, manager, jwtService, encoder );
    }

    @Test
    @DisplayName("When try to register user should be successfully")
    void register_shouldCreateUserSuccessfully(){
        Users user= AuthFactory.createValidAuth();

        RegisterRequest registerRequest=AuthFactory.createRegisterRequest();

        RegisterResponse registerResponse=AuthFactory.createRegisterResponse();

        BDDMockito.given(userRepository.existsByEmail(registerResponse.getEmail())).willReturn(false);
        BDDMockito.given(userRepository.save(user)).willReturn(user);
        BDDMockito.given(usersMap.toUser(registerRequest)).willReturn(user);
        BDDMockito.given(usersMap.toRegisterResponse(user)).willReturn(registerResponse);

        RegisterResponse results = authService.register(registerRequest);

        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results.getEmail()).isEqualTo(registerResponse.getEmail());
        Assertions.assertThat(results.getRoles()).isEqualTo(registerResponse.getRoles());

        BDDMockito.then(userRepository).should().existsByEmail(registerRequest.getEmail());
        BDDMockito.then(userRepository).should().save(user);
        BDDMockito.then(usersMap).should().toUser(registerRequest);
        BDDMockito.then(usersMap).should().toRegisterResponse(user);

    }

    @Test
    @DisplayName("When email not exist throw exception")
    void register_shouldThrowExceptionWhenEmailAlreadyExists(){

        RegisterRequest registerRequest=AuthFactory.createRegisterRequest();

        given(userRepository.existsByEmail(registerRequest.getEmail())).willReturn(true);

        Assertions.assertThatExceptionOfType(BusinessException.class).isThrownBy(()->authService.register(registerRequest));

        then(userRepository).should().existsByEmail(registerRequest.getEmail());
    }


    @Test
    @DisplayName("When try login returns successfully and token")
    void login_shouldReturnTokenWhenCredentialsAreValid(){
        LoginRequest loginRequest=AuthFactory.createLoginRequest();
        UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication= authenticationToken;
        String token= "jwt-token-123";

        given(manager.authenticate(authenticationToken)).willReturn(authentication);
        given(jwtService.generatedToken(authentication.getName())).willReturn(token);

        LoginResponse results = authService.login(loginRequest);

        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results.getToken()).isEqualTo(token);

        then(manager).should().authenticate(authenticationToken);
        then(jwtService).should().generatedToken(authentication.getName());
    }

}
