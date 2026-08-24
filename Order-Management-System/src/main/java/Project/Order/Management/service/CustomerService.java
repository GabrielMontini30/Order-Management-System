package Project.Order.Management.service;

import Project.Order.Management.domain.Customer;
import Project.Order.Management.domain.Order;
import Project.Order.Management.domain.Product;
import Project.Order.Management.domain.Users;
import Project.Order.Management.dto.request.CreateCustomerRequest;
import Project.Order.Management.dto.request.UpdateProductRequest;
import Project.Order.Management.dto.response.CustomerResponse;
import Project.Order.Management.dto.response.ProductResponse;
import Project.Order.Management.exception.ResourceNotFoundException;
import Project.Order.Management.mapStruct.CustomerMap;
import Project.Order.Management.repository.CustomerRepository;
import Project.Order.Management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;
    private final CustomerMap customerMap;
    private final UserRepository userRepository;

    public CustomerResponse create(CreateCustomerRequest request, String email){

        Users userToSave = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("The email not found"));

        Customer customerToSave = customerMap.toEntity(request, userToSave);

        customerToSave.setName(request.getName());

        customerToSave.setPhone(request.getPhone());

        customerToSave.setUsers(userToSave);

        customerToSave.setCreatedAt(new Date(System.currentTimeMillis()));

        repository.save(customerToSave);

        return customerMap.CustomerToResponse(customerToSave);
    }

    public CustomerResponse findById(Long id){
        
        Optional<Customer> customerId = repository.findById(id);

        Customer customer = customerId.orElseThrow(() -> new ResourceNotFoundException("Error, not found by id "));

       return customerMap.CustomerToResponse(customer);
    }

    public List<CustomerResponse> listAll(){
        
        List<Customer> allCustomers = repository.findAll();

        return customerMap.toResponseList(allCustomers);
    }

    public void delete(long id){
        Customer customer = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("The Customer not found"));
        repository.delete(customer);
    }
}
