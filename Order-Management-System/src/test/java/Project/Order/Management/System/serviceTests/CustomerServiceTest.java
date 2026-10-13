package Project.Order.Management.System.serviceTests;

import Project.Order.Management.System.util.AuthFactory;
import Project.Order.Management.System.util.CustomerFactory;
import Project.Order.Management.domain.Customer;
import Project.Order.Management.domain.Users;
import Project.Order.Management.dto.request.CreateCustomerRequest;
import Project.Order.Management.dto.response.CustomerResponse;
import Project.Order.Management.exception.ResourceNotFoundException;
import Project.Order.Management.mapStruct.CustomerMap;
import Project.Order.Management.repository.CustomerRepository;
import Project.Order.Management.repository.ProductRepository;
import Project.Order.Management.repository.UserRepository;
import Project.Order.Management.service.AuthService;
import Project.Order.Management.service.CustomerService;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(SpringExtension.class)
public class CustomerServiceTest {

    @Mock
    private  CustomerRepository customerRepository;
    @Mock
    private  CustomerMap customerMap;
    @Mock
    private  UserRepository userRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);

        customerService= new CustomerService(customerRepository, customerMap, userRepository);
    }

    @Test
    @DisplayName("When try create customer return successfully ")
    void createCustomer_shouldCreateCustomerSuccessfully(){
        Users users= AuthFactory.createValidAuth();
        Customer customer= CustomerFactory.createValidCustomer();
        CreateCustomerRequest customerRequest= CustomerFactory.createValidCustomerRequest();
        CustomerResponse customerResponse=CustomerFactory.createCustomerResponse();

        BDDMockito.given(userRepository.findByEmail(users.getEmail())).willReturn(Optional.of(users));
        BDDMockito.given(customerRepository.save(customer)).willReturn(customer);
        BDDMockito.given(customerMap.toEntity(customerRequest, users)).willReturn(customer);
        BDDMockito.given(customerMap.CustomerToResponse(customer)).willReturn(customerResponse);

        CustomerResponse results = customerService.create(customerRequest, users.getEmail());

        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results.getId()).isEqualTo(customerResponse.getId());
        Assertions.assertThat(results.getName()).isEqualTo(customerResponse.getName());
        Assertions.assertThat(results.getPhone()).isEqualTo(customerResponse.getPhone());


        BDDMockito.then(userRepository).should().findByEmail(users.getEmail());
        BDDMockito.then(customerRepository).should().save(customer);
        BDDMockito.then(customerMap).should().toEntity(customerRequest, users);
        BDDMockito.then(customerMap).should().CustomerToResponse(customer);


    }

    @Test
    @DisplayName("When try to find customer by id return successfully ")
    void findById_shouldFindCustomerByIdSuccessfully(){
        Customer customer= CustomerFactory.createValidCustomer();
        CustomerResponse customerResponse=CustomerFactory.createCustomerResponse();


        BDDMockito.given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));
        BDDMockito.given(customerMap.CustomerToResponse(customer)).willReturn(customerResponse);

        CustomerResponse results = customerService.findById(customer.getId());

        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results.getId()).isEqualTo(customerResponse.getId());
        Assertions.assertThat(results.getName()).isEqualTo(customerResponse.getName());
        Assertions.assertThat(results.getPhone()).isEqualTo(customerResponse.getPhone());

        BDDMockito.then(customerRepository).should().findById(customer.getId());
        BDDMockito.then(customerMap).should().CustomerToResponse(customer);
    }

    @Test
    @DisplayName("When customer not found throw exception ")
    void findById_shouldThrowExceptionWhenCustomerNotFound(){
        given(customerRepository.findById(anyLong())).willReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(()->customerService.findById(1L));

        then(customerRepository).should().findById(1L);
    }

    @Test
    @DisplayName("When ")
    void listAll_shouldListAllCustomersSuccessfully(){
        Customer customer=CustomerFactory.createValidCustomer();

        CustomerResponse customerResponse=CustomerFactory.createCustomerResponse();

        List<Customer> customerList= List.of(customer);

        List<CustomerResponse> customerResponsesList= List.of(customerResponse);

        BDDMockito.given(customerRepository.findAll()).willReturn(customerList);

        BDDMockito.given(customerMap.toResponseList(customerList)).willReturn(customerResponsesList);

        List<CustomerResponse> results = customerService.listAll();

        Assertions.assertThat(results.size()).isEqualTo(1);

        Assertions.assertThat(results.getFirst()).isEqualTo(customerResponse);


        BDDMockito.then(customerRepository).should().findAll();

        BDDMockito.then(customerMap).should().toResponseList(customerList);
    }
}
