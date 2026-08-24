package Project.Order.Management.controller;

import Project.Order.Management.dto.request.CreateCustomerRequest;
import Project.Order.Management.dto.response.CustomerResponse;
import Project.Order.Management.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/customers")
@RequiredArgsConstructor
@RestController
@Tag(name = "Customers", description = "Endpoints to managing customers." )
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    @Operation(summary = "Create customer ", description = "Method for create customer")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "401", description = "Customer isn't authenticated"),
            @ApiResponse(responseCode = "404", description = "Customer not founded")
    })
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest customerRequest, Principal email)
    {
        return new ResponseEntity<>(service.create(customerRequest, email.getName()), HttpStatus.CREATED);
    }

    @GetMapping ("/{id}")
    @Operation(summary = "Find customer by id", description = "Method find customer by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer find successfully"),
            @ApiResponse(responseCode = "401", description = "Customer isn't authenticated"),
            @ApiResponse(responseCode = "404", description = "Customer not founded")
    })
    public ResponseEntity<CustomerResponse> findById(@PathVariable Long id)
    {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "List all customers", description = "Method for list all customers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of customers returned successfully"),
            @ApiResponse(responseCode = "401", description = "Customer isn't authenticated")
    })
    public ResponseEntity<List<CustomerResponse>> listAll()
    {
        return ResponseEntity.ok(service.listAll());
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customers", description = "Method for deleted a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Customers excluded successfully"),
            @ApiResponse(responseCode = "401", description = "Customer isn't authenticated"),
            @ApiResponse(responseCode = "404", description = "Customer not founded")
    })
    public ResponseEntity<Void> delete(@PathVariable long id){

        service.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
