package Project.Order.Management.controller;

import Project.Order.Management.dto.request.CreateProductRequest;
import Project.Order.Management.dto.request.UpdateProductRequest;
import Project.Order.Management.dto.response.ProductResponse;
import Project.Order.Management.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.ListType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Endpoints to managing product.")


public class ProductController {
    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Create product ", description = "Method for create product")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "401", description = "Customer isn't authenticated"),
    })
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request)
    {
        return new ResponseEntity<>(productService.createProduct(request), HttpStatus.CREATED);
    }

    @PutMapping ("/{id}")
    @Operation(summary = "Update product ", description = "Method for update product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product update successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "401", description = "Customer isn't authenticated"),
            @ApiResponse(responseCode = "404", description = "Product not founded")
    })
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody UpdateProductRequest request)
    {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @GetMapping
    @Operation(summary = "List all products", description = "Method for list all products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of products returned successfully"),
            @ApiResponse(responseCode = "401", description = "Customer isn't authenticated")
    })
    public ResponseEntity<List<ProductResponse>> findAll()
    {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping ("/{id}")
    @Operation(summary = "Find product by id", description = "Method find product by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product find successfully"),
            @ApiResponse(responseCode = "401", description = "Customer isn't authenticated"),
            @ApiResponse(responseCode = "404", description = "Product not founded")
    })
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id)
    {
        return ResponseEntity.ok(productService.findById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Method for deleted a product")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product excluded successfully"),
            @ApiResponse(responseCode = "401", description = "Customer isn't authenticated"),
            @ApiResponse(responseCode = "404", description = "Product not founded")
    })
    public ResponseEntity<Void> delete(@PathVariable long id){

        productService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
