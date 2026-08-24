package Project.Order.Management.System.util;

import Project.Order.Management.domain.Users;
import Project.Order.Management.dto.request.LoginRequest;
import Project.Order.Management.dto.request.RegisterRequest;
import Project.Order.Management.dto.response.LoginResponse;
import Project.Order.Management.dto.response.RegisterResponse;
import Project.Order.Management.enums.UserRoles;

import java.util.Date;

public class AuthFactory {
    public static Users createValidAuth(){
        return  Users.builder()
                .id(1l)
                .email("gabrielmontini30@gmail.com")
                .password("gabriel10")
                .createdAt(new Date(System.currentTimeMillis()))
                .roles(UserRoles.USER)
                .build();
    }

    public static LoginRequest createLoginRequest(){
        return LoginRequest.builder()
                .email("gabrielmontini30@gmail.com")
                .password("gabriel10").build();
    }

    public static RegisterRequest createRegisterRequest(){
        return RegisterRequest.builder()
                .email("gabrielmontini30@gmail.com")
                .password("gabriel10")
                .build();
    }

    public static LoginResponse createLoginResponse(){
        return LoginResponse.builder()
                .token("token-fake")
                .build();
    }

    public static RegisterResponse createRegisterResponse(){
        return RegisterResponse.builder()
                .email("gabrielmontini30@gmail.com")
                .roles(UserRoles.USER)
                .build();
    }
}
