package Project.Order.Management.System.util;

import Project.Order.Management.domain.Customer;
import Project.Order.Management.domain.Users;
import Project.Order.Management.dto.request.CreateCustomerRequest;
import Project.Order.Management.dto.response.CustomerResponse;
import Project.Order.Management.enums.UserRoles;

import java.util.Date;

public class CustomerFactory {

    public static Customer createValidCustomer(){
        Users user = Users.builder()
                .id(1L)
                .email("gabriel@gmail.com")
                .password("123456")
                .roles(UserRoles.ADMIN)
                .createdAt(new Date())
                .build();

        return Customer.builder()
                .id(1L)
                .name("test")
                .createdAt(new Date(System.currentTimeMillis()))
                .phone("439989155")
                .users(user)
                .build();
    }

    public static CreateCustomerRequest createValidCustomerRequest(){
        return CreateCustomerRequest.builder()
                .name("test")
                .phone("439989155")
                .build();
    }

    public static CustomerResponse createCustomerResponse(){
        return CustomerResponse.builder()
                .id(1L)
                .name("test")
                .phone("439989155")
                .build();
    }
}
