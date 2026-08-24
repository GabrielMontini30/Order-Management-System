package Project.Order.Management.mapStruct;


import Project.Order.Management.domain.Users;
import Project.Order.Management.dto.request.RegisterRequest;
import Project.Order.Management.dto.response.RegisterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper (componentModel = "spring")

public interface UsersMap {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Users toUser(RegisterRequest request);

    @Mapping(target = "email", source = "email")
    @Mapping(target = "roles", source = "roles")

    RegisterResponse toRegisterResponse(Users users);

}