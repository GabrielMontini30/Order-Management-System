package Project.Order.Management.service;

import Project.Order.Management.domain.OrderItem;
import Project.Order.Management.domain.Product;
import Project.Order.Management.dto.request.CreateProductRequest;
import Project.Order.Management.dto.request.UpdateProductRequest;
import Project.Order.Management.dto.response.ProductResponse;
import Project.Order.Management.exception.ResourceNotFoundException;
import Project.Order.Management.exception.StockException;
import Project.Order.Management.mapStruct.ProductMap;
import Project.Order.Management.repository.ProductRepository;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final  ProductRepository productRepository;
    private final  ProductMap productMap;

    public ProductResponse createProduct(CreateProductRequest productRequest){

        if(productRequest== null) throw new IllegalArgumentException("The product had been completed");

        Product productToSave=productMap.toProduct(productRequest);

        productToSave.setCreatedAt(new Date(System.currentTimeMillis()));

       productRepository.save(productToSave);

        return productMap.toProductResponse(productToSave);

    }

    public ProductResponse updateProduct(Long id, UpdateProductRequest productToUpdate){
        Product oldProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(productToUpdate==null) throw new IllegalArgumentException("Product not null");

        oldProduct.setName(productToUpdate.getName());
        oldProduct.setPrice(productToUpdate.getPrice());
        oldProduct.setDescription(productToUpdate.getDescription());
        oldProduct.setQuantity(productToUpdate.getQuantity());

         productRepository.save(oldProduct);

        return productMap.toProductResponse(oldProduct);
    }

    public ProductResponse findById(Long id) {
        Product productFindById = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return productMap.toProductResponse(productFindById);
    }

    public List<ProductResponse> findAll(){
        List<Product> productsList = productRepository.findAll();
        return productMap.toListProduct(productsList);
    }

    public void delete(long id){

        Product productToDelete = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        productRepository.delete(productToDelete);

    }

    public void verifyStock(List<OrderItem> orderItems){
        for(OrderItem item:orderItems){
            if(item.getProduct()==null){
                throw new ResourceNotFoundException("Product not found in item");
            }
            if(item.getQuantity()>item.getProduct().getQuantity()){
                throw new StockException("Insufficient stock for product: " + item.getProduct().getName());
            }
        }

    }
}
