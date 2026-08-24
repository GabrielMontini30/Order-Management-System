package Project.Order.Management.mapStruct;

import Project.Order.Management.domain.Customer;
import Project.Order.Management.domain.Users;
import Project.Order.Management.dto.request.CreateCustomerRequest;
import Project.Order.Management.dto.response.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper( componentModel = "spring")
public interface CustomerMap {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "users", source = "user")
    Customer toEntity(CreateCustomerRequest customer, Users user);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "phone", source = "phone")
    CustomerResponse CustomerToResponse(Customer customer);

    List<CustomerResponse> toResponseList(List<Customer> customers);
}
