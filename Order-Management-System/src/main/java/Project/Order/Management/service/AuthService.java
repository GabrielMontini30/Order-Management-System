package Project.Order.Management.service;

import Project.Order.Management.domain.Users;
import Project.Order.Management.dto.request.LoginRequest;
import Project.Order.Management.dto.request.RegisterRequest;
import Project.Order.Management.dto.response.LoginResponse;
import Project.Order.Management.dto.response.RegisterResponse;
import Project.Order.Management.enums.UserRoles;
import Project.Order.Management.exception.BusinessException;
import Project.Order.Management.mapStruct.UsersMap;
import Project.Order.Management.repository.UserRepository;
import Project.Order.Management.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UsersMap usersMap;
    private final AuthenticationManager manager;
    private final JwtService jwtService;

    private final PasswordEncoder encoder;

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) throw new BusinessException("The emails found");

        Users user = usersMap.toUser(request);

        user.setPassword(encoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRoles(UserRoles.USER);
        user.setCreatedAt(new Date(System.currentTimeMillis()));

        userRepository.save(user);

        return usersMap.toRegisterResponse(user);
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authenticate = manager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String token = generateToken(authenticate.getName());
        return LoginResponse.builder().token(token).build();
    }

    private String generateToken(String name){
        return jwtService.generatedToken(name);
    }
}