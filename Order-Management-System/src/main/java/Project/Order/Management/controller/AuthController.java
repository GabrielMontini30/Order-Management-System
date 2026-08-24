package Project.Order.Management.controller;

import Project.Order.Management.dto.request.LoginRequest;
import Project.Order.Management.dto.request.RegisterRequest;
import Project.Order.Management.dto.response.LoginResponse;
import Project.Order.Management.dto.response.RegisterResponse;
import Project.Order.Management.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration.")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register costumer",description = "method for register a new costumer ")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Register a new costumer successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Email already registered")
    })
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request){
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping ("/login")
    @Operation(summary = "Login for customer",description = "method for customer login ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer successfully logged in"),
            @ApiResponse(responseCode = "400", description = "invalid request"),
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }
}
